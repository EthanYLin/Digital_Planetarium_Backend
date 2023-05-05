package com.sp04.digital_planetarium.service.serviceImpl;

import com.sp04.digital_planetarium.entity.Figure;
import com.sp04.digital_planetarium.entity.PlayerSocketObject;
import com.sp04.digital_planetarium.entity.Position;
import com.sp04.digital_planetarium.service.PlayerSocketService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PlayerSocketServiceImpl implements PlayerSocketService {
    private final Map<UUID, PlayerSocketObject> players;

    public PlayerSocketServiceImpl() {
        this.players = new HashMap<>();
    }

    public void addPlayer(UUID sessionID, PlayerSocketObject player) {
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

    public List<PlayerSocketObject> getAllPlayers(){
        return this.players.values().stream().toList();
    }


}
