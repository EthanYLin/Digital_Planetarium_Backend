package com.sp04.digital_planetarium.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.sp04.digital_planetarium.entity.*;
import com.sp04.digital_planetarium.service.PlayerSocketService;
import com.sp04.digital_planetarium.service.UserService;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
        User user = userService.findByUid(uid);
        return user == null ? null : user.getUsername();
    }

    private ChatObject errorMsg(String userName, String message){
        return new ChatObject("system", ChatObject.Type.PRIVATE, userName, message);
    }

    private void log_client(SocketIOClient client, String msg){
        System.out.println("-------START------");
        System.out.println("Client: " + clientMap.get(client.getSessionId()));
        System.out.println("SessionID: " + client.getSessionId());
        System.out.println("Msg:" + msg);
        System.out.println("Rooms: " + String.join(", ", client.getAllRooms()));
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

        User user = userService.findByUid(Long.parseLong(uid));
        if(user == null){
            log_client(client, "uid not found");
            client.sendEvent("chat", errorMsg("system", "on connect: uid not found"));
            return;
        }
        String userName = user.getUsername();
        Figure figure = user.getFigure();

        Position initPos = new Position(0.0, 0.0, 0.0);
        PlayerSocketObject playerSocketObject = new PlayerSocketObject(userName, figure, initPos);
        playerSocketService.addPlayer(client.getSessionId(), playerSocketObject);
        clientMap.put(client.getSessionId(), userName);
        log_client(client, "connected.");

        server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());

    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        playerSocketService.removePlayer(client.getSessionId());
        server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());
        System.out.println("Client disconnected: " + client.getSessionId());
    }

    @OnEvent("updatePos")
    public void onUpdatePos(SocketIOClient client, UpdatePosSoekctObject pos){
        playerSocketService.changePosition(client.getSessionId(), pos.getPosition());
        server.getBroadcastOperations().sendEvent("update", playerSocketService.getAllPlayers());
    }

    @OnEvent("updateFig")
    public void onUpdateFig(SocketIOClient client, UpdateFigSocketObject fig){
        playerSocketService.changeFigure(client.getSessionId(), fig.getFigure());
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
    public void onChat(SocketIOClient client, ChatObject chatObject) {
        String from = clientMap.get(client.getSessionId());
        log_client(client, "msg " + chatObject.getType() + " to " + chatObject.getTo() + " : " + chatObject.getMessage());

        if(chatObject.isSomeNull()){
            log_client(client, "some properties of ChatObject is null");
            client.sendEvent("chat", errorMsg(from, "some properties of ChatObject is null"));
            return;
        }
        if(! from.equals(chatObject.getFromUserName())){
            log_client(client, "fromUserName in ChatObject is not equal to the one in Session");
            client.sendEvent("chat", errorMsg(from, "fromUserName in ChatObject is not equal to the one in Session"));
            return;
        }
        if(chatObject.getType() == ChatObject.Type.ROOM && ! client.getAllRooms().contains(chatObject.getTo())){
            log_client(client, "you are not in this room");
            client.sendEvent("chat", errorMsg(from, "you are not in this room"));
            return;
        }
        if(chatObject.getType() == ChatObject.Type.PRIVATE && ! clientMap.containsValue(chatObject.getTo())){
            log_client(client, "the user you want to chat with is not online");
            client.sendEvent("chat", errorMsg(from, "the user you want to chat with is not online"));
            return;
        }

        switch (chatObject.getType()) {
            case PRIVATE -> server.getClient(clientMap.getKey(chatObject.getTo())).sendEvent("chat", chatObject);
            case ROOM -> server.getRoomOperations(chatObject.getTo()).sendEvent("chat", chatObject);
            case BROADCAST -> server.getBroadcastOperations().sendEvent("chat", chatObject);
        }

    }

}