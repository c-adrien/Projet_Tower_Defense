package com.td.app.game;

public class Position {

    private float X;
    private float Y;
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
        return (int) X;
    }

    public int getY() {
        return (int) Y;
    }

    public void setX(float x) {
        X = x;
    }

    public void setY(float y) {
        Y = y;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void updateX(float i){
        this.X += i;
    }

    public void updateY(float i){
        this.Y += i;
    }

}
