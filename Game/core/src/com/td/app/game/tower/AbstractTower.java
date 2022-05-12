package com.td.app.game.tower;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.game.enemy.AbstractEnemy;
import com.td.app.game.map.Tile;


public abstract class AbstractTower extends Actor {

    private int level;
    private static int MAXIMUM_LEVEL;
    private int projectileSpeed;
    private int projectileRange;
    private int timer;
    private int sellPrice;
    private int updatePrice;
    private Tile hostingTile;

    // TODO
    public AbstractEnemy findTarget(){
        return null;
    }

    public abstract void sendProjectile();

    public boolean canUpgrade(){
        return level < MAXIMUM_LEVEL;
    }

    public void upgrade(){

    }

}
