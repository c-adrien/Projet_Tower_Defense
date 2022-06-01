package com.td.app.game.tower;

import com.td.app.game.Position;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.map.Tile;

public class SimpleTower extends AbstractTower {

    private static final String IMAGE_PATH = "textures/tower/simple_tower.png";
    private static final int PROJECTILE_SPEED = 10;
    private static final int PROJECTILE_RANGE = 100;
    public static final int timer = 60;
    private static final int sellPrice = 0;
    private static final int upgradePrice = 0;

    public SimpleTower(Tile hostingTile, int positionX, int positionY) {
        super(PROJECTILE_SPEED, PROJECTILE_RANGE, timer, sellPrice, upgradePrice, hostingTile,
                IMAGE_PATH, positionX, positionY);
    }
    @Override
    public Projectile sendProjectile(StandardEnemy enemy) {
        return new Projectile(enemy, 30, getProjectileSpeed(), new Position(getPosition().getX() + 16, getPosition().getY() + 32));
    }
}
