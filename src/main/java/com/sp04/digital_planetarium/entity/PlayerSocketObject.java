package com.sp04.digital_planetarium.entity;

public class PlayerSocketObject {
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

    private String username;
    private Figure figure;
    private Position position;

    public PlayerSocketObject() {
    }

    public PlayerSocketObject(String username, Figure figure, Position position) {
        this.username = username;
        this.figure = figure;
        this.position = position;
    }


}
