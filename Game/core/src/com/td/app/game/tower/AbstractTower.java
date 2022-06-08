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
     * @param texture tower's texture
     * @param position tower's position
     * @param price tower's price
     */
    public AbstractTower(Texture texture, Position position, int price) {
        this.texture = texture;
        this.position = position;
        this.price = price;
    }

    public StandardEnemy findTarget(ArrayList<StandardEnemy> enemies) {
        StandardEnemy furthestEnnemy = null;

        for (StandardEnemy standardEnemy : enemies) {
            int enemyX = standardEnemy.getPosition().getX();
            int enemyY = standardEnemy.getPosition().getY();

            double distance = Math.sqrt(Math.pow((position.getX() + 16) - enemyX, 2)
                    + Math.pow((position.getY() + 16) - enemyY, 2)
            );

            if(distance < projectileRange) {
                if (furthestEnnemy == null) {
                    furthestEnnemy = standardEnemy;
                } else {
                    furthestEnnemy = furthestEnnemy(furthestEnnemy, standardEnemy);
                }
            }
        }

        return furthestEnnemy;
    }

    private StandardEnemy furthestEnnemy(StandardEnemy furthestEnnemy, StandardEnemy enemy) {
        if (furthestEnnemy.getPosition().getX() > enemy.getPosition().getX()) {
            return furthestEnnemy;
        }
        if (furthestEnnemy.getPosition().getX() < enemy.getPosition().getX()) {
            return enemy;
        }
        if (furthestEnnemy.getPosition().getX() == enemy.getPosition().getX()) {
            if (furthestEnnemy.getPosition().getAngle() == (3 * Math.PI) / 2 // Going downwards
                && enemy.getPosition().getAngle() == (3 * Math.PI) / 2) {
                if (furthestEnnemy.getPosition().getY() > enemy.getPosition().getY()) {
                    return enemy;
                } else {
                    return furthestEnnemy;
                }
            }
            if (furthestEnnemy.getPosition().getAngle() == Math.PI / 2 // Going upwards
                && enemy.getPosition().getAngle() == Math.PI / 2) {
                if (furthestEnnemy.getPosition().getY() < enemy.getPosition().getY()) {
                    return enemy;
                } else {
                    return furthestEnnemy;
                }
            }
        }
        return furthestEnnemy;
    }

    public Projectile sendProjectile(StandardEnemy enemy){
        return new Projectile(enemy, projectileDamage, projectileSpeed,
                new Position(getPosition().getX() + PROJECTILE_OFFSET_X,
                        getPosition().getY() + PROJECTILE_OFFSET_Y)
        );
    }

    public abstract AbstractTower createTower(Tile hostingTile, int positionX, int positionY);

    public boolean canUpgrade(Player player) {
        return level < MAXIMUM_LEVEL && player.getCredit() >= upgradePrice;
    }

    public abstract void upgrade();

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

    public void destroy(){
        texture.dispose();
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
