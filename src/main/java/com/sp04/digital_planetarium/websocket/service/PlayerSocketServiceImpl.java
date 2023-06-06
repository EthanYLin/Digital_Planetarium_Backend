package com.sp04.digital_planetarium.websocket.service;

import com.sp04.digital_planetarium.entity.Figure;
import com.sp04.digital_planetarium.entity.Position;
import com.sp04.digital_planetarium.websocket.entity.Player;
import com.sp04.digital_planetarium.websocket.service.PlayerSocketService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PlayerSocketServiceImpl implements PlayerSocketService {
    private final Map<UUID, Player> players;

    public PlayerSocketServiceImpl() {
        this.players = new HashMap<>();
    }

    public void addPlayer(UUID sessionID, Player player) {
        this.players.put(sessionID, player);
    }

    public void changeFigure(UUID sessionID, Figure figure){
        this.players.get(sessionID).setFigure(figure);
    }

    public void changePosition(UUID sessionID, Position position){
        this.players.get(sessionID).setPosition(position);
    }

    public void removePlayer(UUID sessionID){
        this.players.remove(sessionID);
    }

    public List<Player> getAllPlayers(){
        return this.players.values().stream().toList();
    }


}
