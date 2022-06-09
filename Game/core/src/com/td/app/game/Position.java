package com.td.app.game;

public class Position {
    private float X;
    private float Y;
    private double angle;

    /**
     * Creates a position
     * @param x the x position
     * @param y the y position
     */
    public Position(int x, int y) {
        this(x, y, 0);
    }

    /**
     * Creates a position
     * @param x the x position
     * @param y the y position
     * @param angle the position's angle
     */
    public Position(int x, int y, double angle) {
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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Updates the x position with the actual x position as a base
     * @param i the number to add to the actual x position
     */
    public void updateX(float i){
        this.X += i;
    }

    /**
     * Updates the y position with the actual y position as a base
     * @param i the number to add to the actual y position
     */
    public void updateY(float i){
        this.Y += i;
    }

}
