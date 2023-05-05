package com.sp04.digital_planetarium.service;

import com.sp04.digital_planetarium.entity.Figure;
import com.sp04.digital_planetarium.entity.PlayerSocketObject;
import com.sp04.digital_planetarium.entity.Position;

import java.util.List;
import java.util.UUID;

public interface PlayerSocketService {
    public void addPlayer(UUID sessionID, PlayerSocketObject player);

    public void changeFigure(UUID sessionID, Figure figure);

    public void changePosition(UUID sessionID, Position position);

    public void removePlayer(UUID sessionID);

    public List<PlayerSocketObject> getAllPlayers();

}
