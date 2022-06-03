package com.td.app.game.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.game.Position;
import com.td.app.game.enemy.StandardEnemy;

import java.util.ArrayList;

public class Projectile extends Actor {
    private Texture texture;
    private final Sprite sprite;
    protected int damage;
    private final StandardEnemy target;
    private final int speed;
    private Position position;

    private final int freeze;
    private final int hitNeighboursInRange;

    public Projectile(StandardEnemy target, int damage, int speed, Position position) {
        this(target, damage, speed, position, 0, 0);
    }

    public Projectile(StandardEnemy target, int damage, int speed, Position position, int freeze) {
        this(target, damage, speed, position, freeze, 0);
    }

    public Projectile(StandardEnemy target, int damage, int speed, Position position,
                      int freeze, int hitNeighboursInRange) {
        this.target = target;
        this.damage = damage;
        this.speed = speed;
        this.position = position;
        this.freeze = freeze;
        this.hitNeighboursInRange = hitNeighboursInRange;

        texture = new Texture(Gdx.files.internal("textures/projectile/projectileTest.png"));
        setBounds(0, 0, texture.getWidth(), texture.getHeight());
        sprite = new Sprite(texture);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, position.getX(), position.getY(), position.getX(), position.getY(), getWidth(), getHeight(), 1, 1, 0);
        super.draw(batch, parentAlpha);
    }

    public boolean update(float delta, ArrayList<StandardEnemy> enemyArrayList) {
        if(!target.isAlive()){
            return false;
        }

        if (position.getX() == target.getPosition().getX() && position.getY() == target.getPosition().getY()) {
            target.receiveProjectile(this, enemyArrayList);
            return false;
        }

        delta = delta * speed;

        double delta_x = target.getPosition().getX() - position.getX();
        double delta_y = target.getPosition().getY() - position.getY();

        position.setAngle(Math.atan2(delta_y, delta_x));

        position.updateX((float) (delta * Math.cos(position.getAngle())));
        position.updateY((float) (delta * Math.sin(position.getAngle())));

        return true;
    }

    public int getDamage() {
        return damage;
    }

    public int getFreeze() {
        return freeze;
    }

    public int getHitNeighboursInRange() {
        return hitNeighboursInRange;
    }
}
