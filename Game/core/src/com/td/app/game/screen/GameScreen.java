package com.td.app.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.td.app.TowerDefense;
import com.td.app.game.Game;
import com.td.app.game.map.Map;
import com.td.app.game.map.Tile;
import com.td.app.game.tower.SimpleTower;

public abstract class GameScreen implements Screen, InputProcessor {

    public TowerDefense game;
    protected Game gamePlay;
    private Stage stage;
    private SpriteBatch batch;

    // Debug
    ShapeRenderer shapeRenderer;
    int x = 0;
    int y = 0;

    public GameScreen(TowerDefense game) {
        this.game = game;
        Gdx.input.setInputProcessor(this);

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
    }

    @Override
    public void render(float delta) {
        batch.begin();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        batch.end();

        // Debug
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Red line
        shapeRenderer.line(x, y, x+64, y+64);
        shapeRenderer.end();

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

            int line = y;
            int column = x;

            Tile tile = gamePlay.getMap().getMap()[line][column];

            if(tile.isSelected() && !tile.isOccupied()){
                System.out.println("place x :" + x*64);
                System.out.println("place y :" + y*64);

                SimpleTower simpleTower = new SimpleTower(0, 0, tile, x*64, 704 - y*64);
                stage.addActor(simpleTower);
            }



            gamePlay.getMap().toggleTile(line, column);
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
