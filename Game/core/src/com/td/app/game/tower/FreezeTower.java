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
    public static final int TIMER = 120;
    public static final int price = 100;

    private int FREEZE = 1;

    public FreezeTower(Tile hostingTile, int positionX, int positionY) {
        super(PROJECTILE_SPEED, PROJECTILE_RANGE, PROJECTILE_DAMAGE, TIMER, price, hostingTile,
                IMAGE_PATH, positionX, positionY);
    }

    public FreezeTower(Position position) {
        super(new Texture(Gdx.files.internal(IMAGE_PATH)), position, price);
    }

    /**
     * <p>
     *     Finds the priority target (i.e. the furthest enemy) from the tower
     * </p>
     * <p>
     *     If the priority target is frozen, it focuses the other enemy
     * </p>
     * @param furthestEnemy the last furthest enemy known
     * @param enemy the enemy in tower's range
     * @return the furthest enemy non-frozen
     */
    @Override
    protected StandardEnemy furthestEnemy(StandardEnemy furthestEnemy, StandardEnemy enemy) {
        if (furthestEnemy.getPosition().getX() > enemy.getPosition().getX()) {
            if (furthestEnemy.isFrozen()) {
                return enemy;
            }
            return furthestEnemy;
        }
        if (furthestEnemy.getPosition().getX() < enemy.getPosition().getX()) {
            if (enemy.isFrozen()) {
                return furthestEnemy;
            }
            return enemy;
        }
        if (furthestEnemy.getPosition().getX() == enemy.getPosition().getX()) {
            if (furthestEnemy.getPosition().getAngle() == (3 * Math.PI) / 2 // Enemy goes downwards
                    && enemy.getPosition().getAngle() == (3 * Math.PI) / 2) {
                if (furthestEnemy.getPosition().getY() > enemy.getPosition().getY()) {
                    if (enemy.isFrozen()) {
                        return furthestEnemy;
                    }
                    return enemy;
                } else {
                    if (furthestEnemy.isFrozen()) {
                        return enemy;
                    }
                    return furthestEnemy;
                }
            }
            if (furthestEnemy.getPosition().getAngle() == Math.PI / 2 // Enemy goes upwards
                    && enemy.getPosition().getAngle() == Math.PI / 2) {
                if (furthestEnemy.getPosition().getY() < enemy.getPosition().getY()) {
                    if (enemy.isFrozen()) {
                        return furthestEnemy;
                    }
                    return enemy;
                } else {
                    if (furthestEnemy.isFrozen()) {
                        return enemy;
                    }
                    return furthestEnemy;
                }
            }
        }

        return furthestEnemy;
    }

    @Override
    public void upgrade() {
        level++;
        FREEZE++;
        updatePrices();
    }

    @Override
    public Projectile sendProjectile(StandardEnemy enemy) {
        return new Projectile(enemy, projectileDamage, projectileSpeed,
                new Position(getPosition().getX() + PROJECTILE_OFFSET_X, getPosition().getY() + PROJECTILE_OFFSET_Y),
                FREEZE
        );
    }

    @Override
    public AbstractTower createTower(Tile hostingTile, int positionX, int positionY) {
        return new FreezeTower(hostingTile, positionX, positionY);
    }
}
