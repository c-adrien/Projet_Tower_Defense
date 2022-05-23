package com.td.app.game.enemy;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.game.Position;
import com.td.app.game.tower.Projectile;

public class StandardEnemy extends Actor {

    protected int MAXIMUM_HP;
    protected int HP;
    protected int speed;
    protected Position position;
    protected boolean isAlive;

    public StandardEnemy(int MAXIMUM_HP, int HP, int speed, Position position) {
        this.MAXIMUM_HP = MAXIMUM_HP;
        this.HP = HP;
        this.speed = speed;
        this.position = position;
        this.isAlive = true;
    }

    // TODO
    public void freeze(int time){
        //
    }

    public void updatePosition(){
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
}
