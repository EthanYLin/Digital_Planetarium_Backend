package com.sp04.digital_planetarium.websocket.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.sp04.digital_planetarium.exception.BadRequestException;
import com.sp04.digital_planetarium.websocket.service.PlayerSocketService;
import com.sp04.digital_planetarium.websocket.entity.UpdateUserFig;
import com.sp04.digital_planetarium.websocket.entity.UpdateUserPos;
import com.sp04.digital_planetarium.websocket.entity.Chat;
import com.sp04.digital_planetarium.websocket.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class SocketIOController {

    @Autowired
    private SocketIOServer server;

    @Autowired
    private PlayerSocketService playerSocketService;

    private static final Logger logger = LoggerFactory.getLogger(SocketIOController.class);

    private void sendErrorMsg(SocketIOClient client, String msg){
        Chat chatBody = new Chat("system", Chat.Type.PRIVATE, "", msg);
        client.sendEvent("chat", chatBody);
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        // 获得token
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if(token == null){
            sendErrorMsg(client, "on connect: token not found");
            logger.warn("OnConnect: token not found. Session: {}", client.getSessionId());
            return ;
        }

        // 验证token, 注册用户
        Player player;
        try{
            player = playerSocketService.addPlayer(client.getSessionId(), token);
        } catch (BadRequestException e) {
            sendErrorMsg(client, e.getMessage());
            logger.warn("OnConnect: {} Session: {}", e.getMessage(), client.getSessionId());
            return ;
        }

        // 广播新用户加入
        logger.info("OnConnect: {}, UserCount: {}, Session: {}",
                player.getUsername(), playerSocketService.getPlayerCount(), client.getSessionId());
        server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        logger.info("OnDisconnect: UserCount: {}, Session: {}",
                playerSocketService.getPlayerCount(), client.getSessionId());
        playerSocketService.removePlayer(client.getSessionId());
        server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());
    }

    @OnEvent("updatePos")
    public void onUpdatePos(SocketIOClient client, UpdateUserPos pos){
        try{
            playerSocketService.changePosition(client.getSessionId(), pos.getPosition());
            server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());
        } catch (BadRequestException e) {
            sendErrorMsg(client, e.getMessage());
            logger.warn("OnUpdatePos: {} Session: {}", e.getMessage(), client.getSessionId());
        }
    }

    @OnEvent("updateFig")
    public void onUpdateFig(SocketIOClient client, UpdateUserFig fig){
        try{
            playerSocketService.changeFigure(client.getSessionId(), fig.getFigure());
            server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());
        } catch (BadRequestException e) {
            sendErrorMsg(client, e.getMessage());
            logger.warn("OnUpdateFig: {} Session: {}", e.getMessage(), client.getSessionId());
        }
    }

    @OnEvent("join")
    public void onJoin(SocketIOClient client, String roomName) {
        if (! playerSocketService.existPlayer(client.getSessionId())) {
            sendErrorMsg(client, "on join: player not found");
            logger.warn("OnJoin: player not found. Session: {}", client.getSessionId());
        } else {
            client.joinRoom(roomName);
            logger.info("OnJoin: {}, Session: {}", roomName, client.getSessionId());
        }
    }

    @OnEvent("leave")
    public void onLeave(SocketIOClient client, String roomName) {
        if (! playerSocketService.existPlayer(client.getSessionId())) {
            sendErrorMsg(client, "on leave: player not found");
            logger.warn("OnLeave: player not found. Session: {}", client.getSessionId());
        } else {
            client.leaveRoom(roomName);
            logger.info("OnLeave: {}, Session: {}", roomName, client.getSessionId());
        }
    }

    @OnEvent("chat")
    public void onChat(SocketIOClient client, Chat chat) {
        String fromUserName;
        try {
            fromUserName = playerSocketService.getPlayer(client.getSessionId()).getUsername();
        } catch (BadRequestException e) {
            sendErrorMsg(client, e.getMessage());
            logger.warn("OnChat: {} Session: {}", e.getMessage(), client.getSessionId());
            return;
        }

        String errorMsg = validateChat(client, chat, fromUserName);
        if(errorMsg != null){
            sendErrorMsg(client, errorMsg);
            logger.warn("OnChat: {} Session: {}", errorMsg, client.getSessionId());
            return;
        }

        logger.info("New Msg From {} To {} : {} ", chat.getFromUserName(), chat.getTo(), chat.getMessage());

        switch (chat.getType()) {
            case PRIVATE -> {
                UUID to = playerSocketService.getSessionID(chat.getTo()).orElse(null);
                server.getClient(to).sendEvent("chat", chat);
            }
            case ROOM -> server.getRoomOperations(chat.getTo()).sendEvent("chat", chat);
            case BROADCAST -> server.getBroadcastOperations().sendEvent("chat", chat);
        }

    }

    private String validateChat(SocketIOClient client, Chat chat, String fromUserName) {
        if(chat.isSomeNull()){
            return "some properties of ChatObject is null";
        }
        if(! fromUserName.equals(chat.getFromUserName())){
            return "fromUserName in ChatObject is not equal to the one in Session";
        }
        if(chat.getType() == Chat.Type.ROOM && ! client.getAllRooms().contains(chat.getTo())){
            return "you are not in this room";
        }
        if(chat.getType() == Chat.Type.PRIVATE && ! playerSocketService.existPlayer(chat.getTo())){
            return "the user you want to chat with is not online";
        }
        return null;
    }


}
