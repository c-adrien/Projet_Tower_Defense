package com.td.app.game;

public class Position {
    private int X;
    private int Y;
    private int angle;

    public Position(int x, int y) {
        this(x, y, 0);
    }

    public Position(int x, int y, int angle) {
        X = x;
        Y = y;
        this.angle = angle;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
