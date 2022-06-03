package com.td.app.game.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.td.app.game.Position;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.map.Tile;

public class FreezeTower extends AbstractTower {

    private static final String IMAGE_PATH = "textures/tower/freeze_tower.png";
    private static final int PROJECTILE_SPEED = 200;
    private static final int PROJECTILE_RANGE = 100;
    private static final int PROJECTILE_DAMAGE = 5;
    public static final int TIMER = 50;
    public static final int price = 30;

    private int FREEZE = 1;


    public FreezeTower(Tile hostingTile, int positionX, int positionY) {
        super(PROJECTILE_SPEED, PROJECTILE_RANGE, PROJECTILE_DAMAGE, TIMER, price, hostingTile,
                IMAGE_PATH, positionX, positionY);
    }

    public FreezeTower(Position position) {
        super(new Texture(Gdx.files.internal(IMAGE_PATH)), position, price);
    }

    @Override
    public void upgrade() {
        level++;
        FREEZE++;
    }

    @Override
    public Projectile sendProjectile(StandardEnemy enemy) {
        return new Projectile(enemy, projectileDamage, projectileSpeed,
                new Position(getPosition().getX() + PROJECTILE_OFFSET_X,
                        getPosition().getY() + PROJECTILE_OFFSET_Y), FREEZE);
    }

    @Override
    public AbstractTower createTower(Tile hostingTile, int positionX, int positionY) {
        return new FreezeTower(hostingTile, positionX, positionY);
    }
}
