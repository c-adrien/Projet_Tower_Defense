package com.td.app.game.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.td.app.game.Position;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.map.Tile;

public class SimpleTower extends AbstractTower {

    private static final String IMAGE_PATH = "textures/tower/simple_tower.png";
    private static final int PROJECTILE_SPEED = 200;
    private static final int PROJECTILE_RANGE = 100;
    public static final int TIMER = 60;
    public static final int price = 100;

    public SimpleTower(Tile hostingTile, int positionX, int positionY) {
        super(PROJECTILE_SPEED, PROJECTILE_RANGE, TIMER, price, hostingTile,
                IMAGE_PATH, positionX, positionY);
    }
    public SimpleTower(Position position, int price) {
        super(new Texture(Gdx.files.internal(IMAGE_PATH)), position, price);
    }

    @Override
    public Projectile sendProjectile(StandardEnemy enemy) {
        return new Projectile(enemy, 30, getProjectileSpeed(),
                new Position(getPosition().getX() + PROJECTILE_OFFSET_X, getPosition().getY() + PROJECTILE_OFFSET_Y));
    }

    @Override
    public AbstractTower createTower(Tile hostingTile, int positionX, int positionY) {
        return new SimpleTower(hostingTile, positionX, positionY);
    }
}
