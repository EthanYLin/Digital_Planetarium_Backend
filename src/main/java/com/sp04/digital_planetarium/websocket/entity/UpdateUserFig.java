package com.sp04.digital_planetarium.websocket.entity;

import com.sp04.digital_planetarium.entity.Figure;

public class UpdateUserFig {
    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    private Figure figure;

    public UpdateUserFig() {
    }

    public UpdateUserFig(Figure figure) {
        this.figure = figure;
    }
}
