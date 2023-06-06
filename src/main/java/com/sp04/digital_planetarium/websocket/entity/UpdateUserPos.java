package com.sp04.digital_planetarium.websocket.entity;

import com.sp04.digital_planetarium.entity.Position;

public class UpdateUserPos {
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private Position position;

    public UpdateUserPos() {
    }

    public UpdateUserPos(Position position) {
        this.position = position;
    }

}

