package com.td.app.game.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.SoundHandler;
import com.td.app.game.Position;
import com.td.app.game.map.Map;
import com.td.app.game.map.MapElements;
import com.td.app.game.map.Tile;
import com.td.app.game.tower.Projectile;

import java.util.ArrayList;

public class StandardEnemy extends Actor {
    public final static int TEXTURE_SIZE = 64/2;

    protected int MAXIMUM_HP;
    protected int HP;
    protected HealthBar healthBar;
    protected int creditDeathValue;

    protected int movementSpeed;
    protected Position position;

    private boolean isAlive;

    protected Texture whiteEnemyTexture;

    protected Texture redEnemyTexture;
    private float elapsedTime;

    protected Sprite sprite;
    protected Tile currentTile;

    protected float freezeTime;

    /**
     * Creates an enemy with different stats
     * @param MAXIMUM_HP enemy's maximum HP
     * @param movementSpeed enemy's movement speed
     * @param position enemy's spawning position
     * @param whiteEnemyTexture enemy's texture
     */
    public StandardEnemy(int MAXIMUM_HP, int movementSpeed, Position position, Texture whiteEnemyTexture) {
        this.MAXIMUM_HP = MAXIMUM_HP;
        this.HP = MAXIMUM_HP;
        this.movementSpeed = movementSpeed;
        this.position = position;
        this.isAlive = true;

        this.whiteEnemyTexture = whiteEnemyTexture;
        this.currentTile = null;
        this.freezeTime = 0;
        this.creditDeathValue = 10;

        this.healthBar = new HealthBar(this);

        redEnemyTexture = new Texture(Gdx.files.internal("textures/enemy/test2.png"));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int x = this.position.getX();
        int y = this.position.getY();

        if (x > 12*64 || y > 12*64 || !isAlive) {
            return;
        }

        // Render HealthBar
        healthBar.draw(batch);

        setBounds(position.getX(), position.getY(), TEXTURE_SIZE, TEXTURE_SIZE);

        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime % 1 > 0.5) {
            sprite = new Sprite(whiteEnemyTexture);
        }
        else {
            sprite = new Sprite(redEnemyTexture);
        }

        batch.draw(sprite, position.getX(), position.getY(), position.getX(), position.getY(),
                TEXTURE_SIZE, TEXTURE_SIZE, 1, 1,0);
        super.draw(batch, parentAlpha);
    }

    /**
     * Freezes enemy's movement
     * @param time time for which the enemy is frozen
     */
    public void freeze(int time) {
        this.freezeTime = elapsedTime + time;
    }

    /**
     * Damages all nearby enemies from initial targeted enemy (AOE damage)
     * @param enemyArrayList the enemies impacted by the AOE
     * @param range the range in which enemies are impacted
     * @param damage the amount of damage received
     */
    public void impactNeighbours(ArrayList<StandardEnemy> enemyArrayList, int range, int damage) {
        for (StandardEnemy standardEnemy : enemyArrayList) {
            int enemyX = standardEnemy.getPosition().getX();
            int enemyY = standardEnemy.getPosition().getY();

            double distance = Math.sqrt(Math.pow((position.getX() + 16) - enemyX, 2)
                    + Math.pow((position.getY() + 16) - enemyY, 2)
            );

            if (distance < range) {
                standardEnemy.receiveDamage(damage);
            }
        }
    }

    /**
     * Updates enemy's status when a projectile touch it
     * @param projectile the projectile received
     * @param enemyArrayList the enemies impacted
     */
    public void receiveProjectile(Projectile projectile, ArrayList<StandardEnemy> enemyArrayList) {
        receiveDamage(projectile.getDamage());

        if (projectile.getFreeze() > 0) {
            freeze(projectile.getFreeze());
        }

        if (projectile.getHitNeighboursInRange() > 0) {
            impactNeighbours(enemyArrayList, projectile.getHitNeighboursInRange(), projectile.getDamage()/4);
        }
    }

    /**
     * Updates enemy's health
     * @param damage the amount of damage received
     */
    private void receiveDamage(int damage) {
        this.HP -= damage;
        if (this.HP <= 0) {
            die();
        } else {
            SoundHandler.play("hit_enemy");
        }
    }

    /**
     * Changes enemy's alive status to dead
     */
    public void die() {
        SoundHandler.play("kill_enemy");
        this.isAlive = false;
    }

    /**
     * Tests a value contained in an interval
     * @param low lower bound
     * @param high upper bound
     * @param n value to test
     * @return whether is contained between lower bound (inclusive) and upper bound (inclusive)
     */
    public static boolean intervalContains(int low, int high, int n) {
        return n >= low && n <= high;
    }

    /**
     * Updates enemy's position values
     * @param delta the time in seconds since last render
     * @param map the game's map
     * @return whether the enemy is still on the map
     */
    public boolean update(float delta, Map map) {
        delta = delta * movementSpeed;

        if (freezeTime > elapsedTime) {
            return true;
        }

        if (isAlive) {
            int x = this.position.getX();
            int y = this.position.getY();

            // If leaves map's bound
            if (x >= Map.TOTAL_SIZE || y >= Map.TOTAL_SIZE) {
                this.isAlive = false;
                return false;
            }

            // If enters map's bounds
            if (!(x < 0 || y < 0)) {
                this.currentTile = map.getTileFromPosition(x, 728 - y + 32);

                // Tile (%64) + interval to choose => changes direction in tile's middle way
                if (intervalContains(28, 40, x%64) || intervalContains(28, 40, y%64)) {
                    if (currentTile != null) {
                        if (!currentTile.mapElement.equals(MapElements.CHEMIN_HORIZONTAL)
                                && !currentTile.mapElement.equals(MapElements.CHEMIN_VERTICAL)) {

                            switch (currentTile.mapElement) {
                                case CHEMIN_BAS_DROITE:
                                    this.position.setAngle(0);
                                    break;

                                case CHEMIN_HAUT_DROITE:
                                    this.position.setAngle(0);
                                    break;

                                case CHEMIN_GAUCHE_BAS:
                                    this.position.setAngle((3 * Math.PI)/2);
                                    break;

                                case CHEMIN_GAUCHE_HAUT:
                                    this.position.setAngle(Math.PI/2);
                                    break;
                            }
                        }
                    }
                }
            }

            // New positions
            float i = (float) (delta * Math.cos(position.getAngle()));
            float j = (float) (delta * Math.sin(position.getAngle()));

            this.position.updateX(i);
            this.position.updateY(j);

            return true;
        }

        return false;
    }

    // Getters
    //==================================================

    public int getCreditDeathValue() {
        return creditDeathValue;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
