package com.sp04.digital_planetarium.websocket.service;

import com.sp04.digital_planetarium.entity.Figure;
import com.sp04.digital_planetarium.entity.Position;
import com.sp04.digital_planetarium.websocket.entity.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerSocketService {
    public void addPlayer(UUID sessionID, Player player);

    public void changeFigure(UUID sessionID, Figure figure);

    public void changePosition(UUID sessionID, Position position);

    public void removePlayer(UUID sessionID);

    public List<Player> getAllPlayers();

}
