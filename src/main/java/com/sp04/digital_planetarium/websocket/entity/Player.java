package com.sp04.digital_planetarium.websocket.entity;

import com.sp04.digital_planetarium.entity.Figure;
import com.sp04.digital_planetarium.entity.Position;

public class Player {
    private String username;
    private Figure figure;
    private Position position;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Player() {
    }

    public Player(String username, Figure figure, Position position) {
        this.username = username;
        this.figure = figure;
        this.position = position;
    }

}
