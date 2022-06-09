package com.td.app.game.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HealthBar {
    private final static int BAR_OFFSET = 15;

    private final float maxHealth;
    private float health;

    private int x;
    private int y;

    protected StandardEnemy enemy;

    /**
     * Creates an heath bar owned by an enemy
     * @param enemy the enemy with which the health bar is linked
     */
    public HealthBar(StandardEnemy enemy) {
        this.enemy = enemy;
        this.maxHealth = enemy.MAXIMUM_HP;
    }

    /**
     * Draws the health bar
     * @param batch batch used to draw
     */
    public void draw(Batch batch) {
        updateHealthBarValues();

        int border = (int) (health/maxHealth * StandardEnemy.TEXTURE_SIZE);
        if (border < 0 || border > StandardEnemy.TEXTURE_SIZE) {
            throw new IllegalArgumentException("Negative HealthBar Error");
        }

        batch.end(); // Pause batch drawing and start shape drawing

        // Init renderer
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Green
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y, border, 2);
        shapeRenderer.end();

        // Red
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x + border, y, StandardEnemy.TEXTURE_SIZE - border, 2);
        shapeRenderer.end();

        batch.begin(); // Restart batch drawing
    }

    /**
     * Update enemy's health bar
     */
    private void updateHealthBarValues() {
        this.health = enemy.HP;
        this.x = enemy.position.getX();
        this.y = enemy.position.getY() + StandardEnemy.TEXTURE_SIZE + BAR_OFFSET;
    }
}
