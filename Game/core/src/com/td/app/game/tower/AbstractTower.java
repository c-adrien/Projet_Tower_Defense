package com.td.app.game.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.game.Player;
import com.td.app.game.Position;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.map.Tile;

import java.util.ArrayList;

public abstract class AbstractTower extends Actor {
    public static int GAME_SPEED = 1;

    protected int level = 1;
    public static final int MAXIMUM_LEVEL = 5;
    protected static final int PROJECTILE_OFFSET_X = 16;
    protected static final int PROJECTILE_OFFSET_Y = 16;
    protected int projectileSpeed;
    protected int projectileRange;
    protected int projectileDamage;

    private int INITIAL_TIMER;
    private int timer;
    private int price;
    private int sellPrice;
    private int upgradePrice;
    private Tile hostingTile;
    private boolean isSelected;

    private Position position;


    private Texture texture;
    private Sprite sprite;

    /**
     * Creates a tower with different stats
     * @param projectileSpeed the projectile's speed
     * @param projectileRange the projectile's range
     * @param projectileDamage the projectile's amount of damage
     * @param timer the delay between two tower shots
     * @param price the tower's price
     * @param hostingTile the tile where the tower is set
     * @param imgPath the path to the texture's location
     * @param X the tower's x position
     * @param Y the tower's y position
     */
    public AbstractTower(int projectileSpeed, int projectileRange, int projectileDamage, int timer, int price,
                         Tile hostingTile, String imgPath, int X, int Y) {
        this.projectileSpeed = projectileSpeed;
        this.projectileRange = projectileRange;
        this.projectileDamage = projectileDamage;

        this.timer = timer;
        this.INITIAL_TIMER = timer;

        this.price = price;
        this.sellPrice = Math.round(0.7F * price);
        this.upgradePrice = price + (10 * level);

        this.hostingTile = hostingTile;
        hostingTile.setOccupied(true);
        this.texture = new Texture(Gdx.files.internal(imgPath));
        this.position = new Position(X, Y);
    }

    /**
     * Constructor for tower's texture only in shop
     * @param texture the tower's texture
     * @param position the tower's position
     * @param price the tower's price
     */
    public AbstractTower(Texture texture, Position position, int price) {
        this.texture = texture;
        this.position = position;
        this.price = price;
    }

    /**
     * Finds the enemies in the tower's range
     * @param enemies the enemies on stage
     * @return the furthest enemy from the tower
     */
    public StandardEnemy findTarget(ArrayList<StandardEnemy> enemies) {
        StandardEnemy furthestEnemy = null;

        for (StandardEnemy standardEnemy : enemies) {
            int enemyX = standardEnemy.getPosition().getX();
            int enemyY = standardEnemy.getPosition().getY();

            double distance = Math.sqrt(Math.pow((position.getX() + 16) - enemyX, 2)
                    + Math.pow((position.getY() + 16) - enemyY, 2)
            );

            if(distance < projectileRange) {
                if (furthestEnemy == null) {
                    furthestEnemy = standardEnemy;
                } else {
                    furthestEnemy = furthestEnemy(furthestEnemy, standardEnemy);
                }
            }
        }

        return furthestEnemy;
    }

    /**
     * Finds the priority target (i.e. the furthest enemy) from the tower
     * @param furthestEnemy the last furthest enemy known
     * @param enemy the enemy in tower's range
     * @return the furthest enemy between the both
     */
    protected StandardEnemy furthestEnemy(StandardEnemy furthestEnemy, StandardEnemy enemy) {
        if (furthestEnemy.getPosition().getX() > enemy.getPosition().getX()) {
            return furthestEnemy;
        }
        if (furthestEnemy.getPosition().getX() < enemy.getPosition().getX()) {
            return enemy;
        }
        if (furthestEnemy.getPosition().getX() == enemy.getPosition().getX()) {
            if (furthestEnemy.getPosition().getAngle() == (3 * Math.PI) / 2 // Enemy goes downwards
                && enemy.getPosition().getAngle() == (3 * Math.PI) / 2) {
                if (furthestEnemy.getPosition().getY() > enemy.getPosition().getY()) {
                    return enemy;
                } else {
                    return furthestEnemy;
                }
            }
            if (furthestEnemy.getPosition().getAngle() == Math.PI / 2 // Enemy goes upwards
                && enemy.getPosition().getAngle() == Math.PI / 2) {
                if (furthestEnemy.getPosition().getY() < enemy.getPosition().getY()) {
                    return enemy;
                } else {
                    return furthestEnemy;
                }
            }
        }
        return furthestEnemy;
    }

    /**
     * Creates and sends a projectile towards an enemy
     * @param enemy the enemy targeted
     * @return a new projectile
     */
    public Projectile sendProjectile(StandardEnemy enemy){
        return new Projectile(enemy, projectileDamage, projectileSpeed,
                new Position(getPosition().getX() + PROJECTILE_OFFSET_X,
                        getPosition().getY() + PROJECTILE_OFFSET_Y)
        );
    }

    /**
     * Creates a new tower at the specified position
     * @param hostingTile the tile where the tower is set
     * @param positionX the tower's x position
     * @param positionY the tower's y position
     * @return a new tower
     */
    public abstract AbstractTower createTower(Tile hostingTile, int positionX, int positionY);

    /**
     * Checks the tower's upgradability depending on its level and player's credit
     * @param player the player of the game
     * @return whether the tower is upgradable
     */
    public boolean canUpgrade(Player player) {
        return level < MAXIMUM_LEVEL && player.getCredit() >= upgradePrice;
    }

    /**
     * Upgrades tower's specific stats
     */
    public abstract void upgrade();

    /**
     * Updates tower's sell price and upgrade price when the tower is upgraded
     */
    public void updatePrices() {
        sellPrice = Math.round(0.7F * upgradePrice); // Actual turret's price after upgrade
        upgradePrice = price + (10 * level);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isSelected) {
            drawRange(batch);
        }

        setBounds(position.getX(), position.getY(), texture.getWidth(), texture.getHeight());
        sprite = new Sprite(texture);

        batch.draw(sprite, position.getX(), position.getY(), position.getX(), position.getY(),
                texture.getWidth(), texture.getHeight(), 1, 1,0
        );

        super.draw(batch, parentAlpha);
    }

    /**
     * Draws the tower's range on stage
     * @param batch the batch used to draw
     */
    private void drawRange(Batch batch) {
        batch.end(); // Pause batch drawing and start shape drawing

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.circle(position.getX() + 32, position.getY() + 32, projectileRange);
        shapeRenderer.end();

        batch.begin(); // Restart batch drawing
    }

    // Getter & Setter
    //========================================================

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTimer() {
        return timer / GAME_SPEED;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getPrice() {
        return price;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public int getUpgradePrice() {
        return upgradePrice;
    }

    public Tile getHostingTile() {
        return hostingTile;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getINITIAL_TIMER() {
        return INITIAL_TIMER;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
