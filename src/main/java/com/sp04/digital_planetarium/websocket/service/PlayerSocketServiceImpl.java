package com.sp04.digital_planetarium.websocket.service;

import com.sp04.digital_planetarium.entity.Figure;
import com.sp04.digital_planetarium.entity.Position;
import com.sp04.digital_planetarium.entity.User;
import com.sp04.digital_planetarium.exception.BadRequestException;
import com.sp04.digital_planetarium.service.UserService;
import com.sp04.digital_planetarium.utils.JwtUtils;
import com.sp04.digital_planetarium.websocket.controller.SocketIOController;
import com.sp04.digital_planetarium.websocket.entity.Player;
import com.sp04.digital_planetarium.websocket.service.PlayerSocketService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerSocketServiceImpl implements PlayerSocketService {
    private final Map<UUID, Player> players = new HashMap<>();


    @Autowired
    private UserService userService;

    public Player addPlayer(UUID sessionID, String token) throws BadRequestException {
        User user;

        try{
            Claims claims = JwtUtils.parseJWT(token);
            Long uid = Long.parseLong(claims.getSubject());
            user = userService.findByUid(uid).orElseThrow(() -> new BadRequestException("WebSocket登录令牌无效", null));
        } catch (Exception e){
            throw new BadRequestException("WebSocket登录令牌无效", null);
        }

        Position initPos = new Position(0.0, 0.0, 0.0);
        Player player = new Player(user.getUsername(), user.getFigure() , initPos);
        players.put(sessionID, player);
        return player;
    }

    public void removePlayer(UUID sessionID) {
        players.remove(sessionID);
    }

    public Player getPlayer(UUID sessionID) throws BadRequestException{
        Player player = players.get(sessionID);
        if (player == null) {
            throw new BadRequestException("SessionID无效", null);
        }
        return player;
    }

    public Optional<Player> getPlayer(String username) {
        return players.values().stream()
                .filter(player -> player.getUsername().equals(username))
                .findFirst();
    }

    public Optional<UUID> getSessionID(String username) {
        return players.entrySet().stream()
                .filter(entry -> entry.getValue().getUsername().equals(username))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public void changeFigure(UUID sessionID, Figure figure) throws BadRequestException {
        if (!players.containsKey(sessionID)) {
            throw new BadRequestException("SessionID无效", null);
        }
        // 实时操作
        players.get(sessionID).setFigure(figure);

        // 数据持久层操作
        User user = userService.findByUsername(players.get(sessionID).getUsername())
                .orElseThrow(() -> new BadRequestException("SessionID无效", null));
        user.setFigure(figure);
        userService.update(user);
    }

    public void changePosition(UUID sessionID, Position position) throws BadRequestException {
        if (!players.containsKey(sessionID)) {
            throw new BadRequestException("SessionID无效", null);
        }
        players.get(sessionID).setPosition(position);
    }

    public List<Player> getAllPlayers() {
        return new ArrayList<>(players.values());
    }

    public int getPlayerCount() {
        return players.size();
    }

    public boolean existPlayer(UUID sessionID) {
        return players.containsKey(sessionID);
    }

    public boolean existPlayer(String username) {
        return players.values().stream()
                .anyMatch(player -> player.getUsername().equals(username));
    }
}
