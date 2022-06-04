package com.td.app.game.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.td.app.game.Position;
import com.td.app.game.map.Tile;

public class MoreDamageTower extends AbstractTower{

    private static final String IMAGE_PATH = "textures/tower/simple_tower_2.png";
    private static final int PROJECTILE_SPEED = 200;
    private static final int PROJECTILE_RANGE = 100;
    private static final int PROJECTILE_DAMAGE = 100;
    public static final int TIMER = 150;
    public static final int price = 50;

    public MoreDamageTower(Tile hostingTile, int positionX, int positionY) {
        super(PROJECTILE_SPEED, PROJECTILE_RANGE, PROJECTILE_DAMAGE, TIMER, price, hostingTile,
                IMAGE_PATH, positionX, positionY);
    }
    public MoreDamageTower(Position position) {
        super(new Texture(Gdx.files.internal(IMAGE_PATH)), position, price);
    }

    @Override
    public AbstractTower createTower(Tile hostingTile, int positionX, int positionY) {
        return new MoreDamageTower(hostingTile, positionX, positionY);
    }

    @Override
    public void upgrade() {
        level++;
        projectileDamage += 25;
    }
}
