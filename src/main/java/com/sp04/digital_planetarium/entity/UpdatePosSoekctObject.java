package com.sp04.digital_planetarium.entity;

public class UpdatePosSoekctObject {
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private Position position;

    public UpdatePosSoekctObject() {
    }

    public UpdatePosSoekctObject(Position position) {
        this.position = position;
    }

}

