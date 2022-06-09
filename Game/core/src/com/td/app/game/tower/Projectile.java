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

    /**
     * Creates a simple projectile with different stats
     * @param target the enemy target
     * @param damage the projectile's amount of damage
     * @param speed the projectile's speed
     * @param position the projectile's initial position
     */
    public Projectile(StandardEnemy target, int damage, int speed, Position position) {
        this(target, damage, speed, position, 0, 0);
    }

    /**
     * Creates a freezing projectile with different stats
     * @param target the enemy target
     * @param damage the projectile's amount of damage
     * @param speed the projectile's speed
     * @param position the projectile's initial position
     * @param freeze the time the target will be frozen
     */
    public Projectile(StandardEnemy target, int damage, int speed, Position position, int freeze) {
        this(target, damage, speed, position, freeze, 0);
    }

    /**
     * Creates a freezing or AOE projectile with different stats
     * @param target the enemy target
     * @param damage the projectile's amount of damage
     * @param speed the projectile's speed
     * @param position the projectile's initial position
     * @param freeze the time the target will be frozen
     * @param hitNeighboursInRange the area where the enemies are damaged
     */
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

    /**
     * Updates the projectile's position on stage
     * @param delta the time in seconds since the last render
     * @param enemyArrayList the enemies hit by the projectile
     * @return whether the projectile hits the enemy
     */
    public boolean update(float delta, ArrayList<StandardEnemy> enemyArrayList) {
        if(!target.isAlive()){
            return true;
        }

        delta = delta * speed;

        double delta_x = target.getPosition().getX() - position.getX();
        double delta_y = target.getPosition().getY() - position.getY();

        position.setAngle(Math.atan2(delta_y, delta_x));

        position.updateX((float) (delta * Math.cos(position.getAngle())));
        position.updateY((float) (delta * Math.sin(position.getAngle())));

        if (position.getX() == target.getPosition().getX() && position.getY() == target.getPosition().getY()) {
            target.receiveProjectile(this, enemyArrayList);
            return true;
        }

        return false;
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
