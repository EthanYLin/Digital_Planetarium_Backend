package com.sp04.digital_planetarium.websocket.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.sp04.digital_planetarium.entity.*;
import com.sp04.digital_planetarium.service.UserService;
import com.sp04.digital_planetarium.entity.User;
import com.sp04.digital_planetarium.websocket.service.PlayerSocketService;
import com.sp04.digital_planetarium.websocket.entity.UpdateUserFig;
import com.sp04.digital_planetarium.websocket.entity.UpdateUserPos;
import com.sp04.digital_planetarium.websocket.entity.Chat;
import com.sp04.digital_planetarium.websocket.entity.Player;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.UUID;

@Controller
public class SocketIOController {

    @Autowired
    private SocketIOServer server;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerSocketService playerSocketService;

    //存储SessionID和UserName的映射关系
    BidiMap<UUID, String> clientMap = new DualHashBidiMap<>();

    private String getUserNameFromUid(Long uid){
        Optional<User> user = userService.findByUid(uid);
        return user.map(User::getUsername).orElse(null);
    }

    private Chat errorMsg(String userName, String message){
        return new Chat("system", Chat.Type.PRIVATE, userName, message);
    }

    private void log_client(SocketIOClient client, String msg){
        System.out.println("-------START------");
        System.out.println("Client: " + clientMap.get(client.getSessionId()));
        System.out.println("SessionID: " + client.getSessionId());
        System.out.println("Msg:" + msg);
        // System.out.println("Rooms: " + String.join(", ", client.getAllRooms()));
        System.out.println("-------END------");
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String uid = client.getHandshakeData().getSingleUrlParam("uid");
        try {
            Long.parseLong(uid);
        } catch (NumberFormatException e) {
            log_client(client, "uid is not a number");
            client.sendEvent("chat", errorMsg("system", "on connect: uid is not a number"));
            return;
        }

        Optional<User> user = userService.findByUid(Long.parseLong(uid));
        if(user.isEmpty()){
            log_client(client, "uid not found");
            client.sendEvent("chat", errorMsg("system", "on connect: uid not found"));
            return;
        }
        String userName = user.get().getUsername();
        Figure figure = user.get().getFigure();

        Position initPos = new Position(0.0, 0.0, 0.0);
        Player player = new Player(userName, figure, initPos);
        playerSocketService.addPlayer(client.getSessionId(), player);
        clientMap.put(client.getSessionId(), userName);

        log_client(client, "connected.");
        System.out.println("Broadcast: New User Connected. User Count: " + playerSocketService.getAllPlayers().size());
        server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());

    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        playerSocketService.removePlayer(client.getSessionId());
        server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());
        System.out.println("Client disconnected: " + client.getSessionId());
        System.out.println("Broadcast: User Disconnected. User Count: " + playerSocketService.getAllPlayers().size());
    }

    @OnEvent("updatePos")
    public void onUpdatePos(SocketIOClient client, UpdateUserPos pos){
        playerSocketService.changePosition(client.getSessionId(), pos.getPosition());
        System.out.println("Broadcast: User " + clientMap.get(client.getSessionId()) + " update position. User Count: " + playerSocketService.getAllPlayers().size());
        server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());
    }

    @OnEvent("updateFig")
    public void onUpdateFig(SocketIOClient client, UpdateUserFig fig){
        //TODO: 数据持久层操作
        //实时操作
        playerSocketService.changeFigure(client.getSessionId(), fig.getFigure());
        System.out.println("Broadcast: User " + clientMap.get(client.getSessionId()) + " update figure. User Count: " + playerSocketService.getAllPlayers().size());
        server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());
    }

    @OnEvent("join")
    public void onJoin(SocketIOClient client, String roomName) {
        client.joinRoom(roomName);
        log_client(client, "join " + roomName);
    }

    @OnEvent("leave")
    public void onLeave(SocketIOClient client, String roomName) {
        client.leaveRoom(roomName);
        log_client(client, "leave " + roomName);
    }

    @OnEvent("chat")
    public void onChat(SocketIOClient client, Chat chat) {
        String from = clientMap.get(client.getSessionId());
        log_client(client, "msg " + chat.getType() + " to " + chat.getTo() + " : " + chat.getMessage());

        if(chat.isSomeNull()){
            log_client(client, "some properties of ChatObject is null");
            client.sendEvent("chat", errorMsg(from, "some properties of ChatObject is null"));
            return;
        }
        if(! from.equals(chat.getFromUserName())){
            log_client(client, "fromUserName in ChatObject is not equal to the one in Session");
            client.sendEvent("chat", errorMsg(from, "fromUserName in ChatObject is not equal to the one in Session"));
            return;
        }
        if(chat.getType() == Chat.Type.ROOM && ! client.getAllRooms().contains(chat.getTo())){
            log_client(client, "you are not in this room");
            client.sendEvent("chat", errorMsg(from, "you are not in this room"));
            return;
        }
        if(chat.getType() == Chat.Type.PRIVATE && ! clientMap.containsValue(chat.getTo())){
            log_client(client, "the user you want to chat with is not online");
            client.sendEvent("chat", errorMsg(from, "the user you want to chat with is not online"));
            return;
        }

        switch (chat.getType()) {
            case PRIVATE -> server.getClient(clientMap.getKey(chat.getTo())).sendEvent("chat", chat);
            case ROOM -> server.getRoomOperations(chat.getTo()).sendEvent("chat", chat);
            case BROADCAST -> server.getBroadcastOperations().sendEvent("chat", chat);
        }

    }

}
