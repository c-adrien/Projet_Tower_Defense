package com.td.app.game.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.map.Tile;


public abstract class AbstractTower extends Actor {

    private int level;
    private static int MAXIMUM_LEVEL = 5;
    private int projectileSpeed;
    private int projectileRange;
    private int timer;
    private int sellPrice;
    private int updatePrice;
    private final Tile hostingTile;
    private final int x;
    private final int y;

    private Texture texture;
    private Sprite sprite;

    public AbstractTower(int projectileSpeed, int projectileRange, int timer, int sellPrice, int updatePrice,
                         Tile hostingTile, String imgPath, int X, int Y) {
        this.level = 1;
        this.projectileSpeed = projectileSpeed;
        this.projectileRange = projectileRange;
        this.timer = timer;
        this.sellPrice = sellPrice;
        this.updatePrice = updatePrice;
        this.hostingTile = hostingTile;

        this.texture = new Texture(Gdx.files.internal(imgPath));

        this.x = X;
        this.y = Y;
    }

    // TODO
    public StandardEnemy findTarget(){
        return null;
    }

    public abstract void sendProjectile();

    public boolean canUpgrade(){
        return level < MAXIMUM_LEVEL;
    }

    public void upgrade(){
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setBounds(0, 0, texture.getWidth(), texture.getHeight());
        sprite = new Sprite(texture);

        batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1 ,0);
        super.draw(batch, parentAlpha);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
