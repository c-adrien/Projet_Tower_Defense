package com.td.app.game.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.td.app.game.Position;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.map.Tile;

public class BombTower extends AbstractTower {

    private static final String IMAGE_PATH = "textures/tower/bomb_tower.png";
    private static final int PROJECTILE_SPEED = 100;
    private static final int PROJECTILE_RANGE = 200;
    private static final int PROJECTILE_DAMAGE = 40;
    public static final int TIMER = 100;
    public static final int price = 40;

    private int HIT_NEIGHBOURS_IN_RANGE = 100;

    public BombTower(Tile hostingTile, int positionX, int positionY) {
        super(PROJECTILE_SPEED, PROJECTILE_RANGE, PROJECTILE_DAMAGE, TIMER, price, hostingTile,
                IMAGE_PATH, positionX, positionY);
    }

    public BombTower(Position position) {
        super(new Texture(Gdx.files.internal(IMAGE_PATH)), position, price);
    }

    @Override
    public void upgrade() {
        level++;
        projectileDamage += 10;
    }

    @Override
    public Projectile sendProjectile(StandardEnemy enemy) {
        return new Projectile(enemy, projectileDamage, projectileSpeed,
                new Position(getPosition().getX() + PROJECTILE_OFFSET_X,
                        getPosition().getY() + PROJECTILE_OFFSET_Y), 0, HIT_NEIGHBOURS_IN_RANGE);
    }
}
