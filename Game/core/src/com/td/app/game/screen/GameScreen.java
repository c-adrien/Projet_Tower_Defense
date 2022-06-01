package com.td.app.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.td.app.TowerDefense;
import com.td.app.game.Game;
import com.td.app.game.Position;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.map.Map;
import com.td.app.game.map.Tile;
import com.td.app.game.tower.AbstractTower;

public abstract class GameScreen implements Screen, InputProcessor {

    public TowerDefense game;
    protected Game gamePlay;
    private Stage stage;
    private SpriteBatch batch;

    // Debug
    ShapeRenderer shapeRenderer;
    int x = 0;
    int y = 0;
    StandardEnemy enemy;
    float i = 0;
    float j = 0;

    public GameScreen(TowerDefense game) {
        this.game = game;

        // Debug
        shapeRenderer = new ShapeRenderer();
    }

    public void initGamePlay(Map map){
        this.gamePlay = new Game(map);
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        stage.addActor(gamePlay.getMap());
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        gamePlay.update(stage, delta);

        batch.begin();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        batch.end();

        // Debug
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Selected tile
        shapeRenderer.setColor(1, 0, 0, 1); // Red line
        shapeRenderer.line(x, y, x+64, y+64);
        shapeRenderer.end();

        // Enemy
        if(enemy != null){
            i = enemy.getPosition().getX();
            j = enemy.getPosition().getY();
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 1, 1); // Blue line
        shapeRenderer.line(i, j, i+32, j+32);
        shapeRenderer.end();

        /*
        // Grid
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int k = 0; k < 24; k++) {
            shapeRenderer.setColor(0, 0, 1, 1); // Blue line
            shapeRenderer.line(0, k*32, 768, k*32);
        }
        shapeRenderer.end();
         */

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("screen X : " + screenX);
        System.out.println("screen Y : " + screenY);
        System.out.println("pointer : " + pointer);
        System.out.println("button : " + button + "\n");

        if(screenX < 768 && screenY < 768){
            int x = screenX / 64;
            int y = screenY / 64;

//
//            int line = y;
//            int column = x;

            Tile tile = gamePlay.getMap().getTileFromPosition(screenX, screenY);

            if(tile.isSelected() && !tile.isOccupied()){

                AbstractTower tower = gamePlay.placeTower(tile, screenX, screenY);
                stage.addActor(tower);

                // Debug
                enemy = new StandardEnemy(50, 1, new Position(-64,
                        gamePlay.getMap().getEntryTilePosition().getY()+32),
                        new Texture(Gdx.files.internal("textures/enemy/test.png")));

                // enemy = new StandardEnemy(60, 10, new Position(entryTilePosition.getX(), entryTilePosition.getY()+32),
                //                        new Texture(Gdx.files.internal("textures/enemy/test.png")));

                gamePlay.addEnemy(enemy);
                stage.addActor(enemy);
            }

            gamePlay.getMap().toggleTile(tile);
            System.out.println(Tile.SELECTED_TILE);

            // Debug
            this.x = x*64;
            this.y = 704 - y*64;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
