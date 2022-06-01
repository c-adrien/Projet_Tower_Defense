package com.td.app.game.tower;

import com.badlogic.gdx.graphics.Texture;
import com.td.app.game.Position;
import com.td.app.game.enemy.StandardEnemy;

public class Projectile {

    private static final int PROJECTILE_OFFSET_X = 15;
    private static final int PROJECTILE_OFFSET_Y = 20;

    protected int damage;
    protected int speed;

    private boolean reachedDestination;

    private StandardEnemy target;
    private Position position;
    private Position destination;
    private Texture texture;

    public Projectile(int damage, int speed, Position position, StandardEnemy target, Texture texture) {
        this.damage = damage;
        this.speed = speed;

        this.target = target;
        this.destination = target.getPosition();
        int changeX = destination.getX() - position.getX() + PROJECTILE_OFFSET_X;
        int changeY = destination.getY() - position.getY() + PROJECTILE_OFFSET_Y;

        this.position = new Position(position.getX() + PROJECTILE_OFFSET_X, position.getY() + PROJECTILE_OFFSET_Y,
                Math.atan2(changeY, changeX));
        this.texture = texture;
    }

    public void update(float delta){
        delta = delta + 10;
        float i = (float) (delta * Math.cos(position.getAngle()));
        float j = (float) (delta * Math.sin(position.getAngle()));

        updatePosition(i, j);
    }

    public void updatePosition(float i, float j){
        this.position.updateX(i);
        this.position.updateY(j);
    }

    public int getDamage() {
        return damage;
    }

    public boolean hasReachedDestination() {
        return reachedDestination;
    }

    public Position getPosition() {
        return position;
    }

    public Position getDestination() {
        return destination;
    }

    public Texture getTexture() {
        return texture;
    }

    public StandardEnemy getTarget() {
        return target;
    }
}
