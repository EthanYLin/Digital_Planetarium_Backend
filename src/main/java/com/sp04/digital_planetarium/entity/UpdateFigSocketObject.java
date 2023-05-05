package com.sp04.digital_planetarium.entity;

public class UpdateFigSocketObject {
    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    private Figure figure;

    public UpdateFigSocketObject() {
    }

    public UpdateFigSocketObject(Figure figure) {
        this.figure = figure;
    }
}
