package com.td.app.game.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.game.Position;
import com.td.app.game.tower.Projectile;

import java.util.ArrayList;

public class StandardEnemy extends Actor {

    public static ArrayList<StandardEnemy> enemies = new ArrayList<>();

    protected int MAXIMUM_HP;
    protected int HP;
    protected int speed;
    protected Position position;
    protected boolean isAlive;
    protected Texture texture;
    protected Sprite sprite;

    public StandardEnemy(int MAXIMUM_HP, int HP, int speed, Position position, Texture texture) {
        this.MAXIMUM_HP = MAXIMUM_HP;
        this.HP = HP;
        this.speed = speed;
        this.position = position;
        this.isAlive = true;

        this.texture = texture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        setBounds(0, 0, texture.getWidth(), texture.getHeight());
        sprite = new Sprite(texture);

        batch.draw(sprite, position.getX(), position.getY(), position.getX(), position.getY(),
                texture.getWidth(), texture.getHeight(), 1, 1,0);
        super.draw(batch, parentAlpha);
    }

    // TODO
    public void freeze(int time){
        //
    }

    public void receiveProjectile(Projectile projectile){
        receiveDamage(projectile.getDamage());
    }

    private void receiveDamage(int damage){
        this.HP -= damage;
        if (this.HP <= 0){
            die();
        }
    }

    public void die(){
        this.isAlive = false;
    }

    public void update(float delta){
        if(isAlive) {

            float i = (float) (delta * Math.cos(position.getAngle()));
            float j = (float) (delta * Math.sin(position.getAngle()));

            updatePosition(i, j);
        }
    }

    public void updatePosition(float i, float j){
        this.position.updateX(i);
        this.position.updateY(j);
    }

    //==================================================

    public static void addEnemy(StandardEnemy enemy){
        enemies.add(enemy);
    }

    public static void removeEnemy(StandardEnemy enemy){
        enemies.remove(enemy);
    }

    public static void updateEnemies(float delta){
        delta *= 10;

        // faster debug
        delta *= 10;

        for (StandardEnemy enemy: enemies) {
            enemy.update(delta);
        }
    }
}
