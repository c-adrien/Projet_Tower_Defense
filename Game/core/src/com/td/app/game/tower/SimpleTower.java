package com.td.app.game.tower;

import com.td.app.game.map.Tile;

public class SimpleTower extends AbstractTower {

    private static final String IMAGE_PATH = "textures/tower/simple_tower.png";
    private static final int PROJECTILE_SPEED = 1;
    private static final int PROJECTILE_RANGE = 1;
    private static final int timer = 1;

    public SimpleTower(int sellPrice, int updatePrice, Tile hostingTile, int x, int y) {
        super(PROJECTILE_SPEED, PROJECTILE_RANGE, timer, sellPrice, updatePrice, hostingTile,
                IMAGE_PATH, x, y);
    }

    @Override
    public void sendProjectile() {
    }
}