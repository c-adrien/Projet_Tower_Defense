package com.td.app.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.td.app.TowerDefense;
import com.td.app.game.map.Map;

public class CampaignGameScreen implements Screen, InputProcessor {
    public TowerDefense game;
    private Stage stage;
    private SpriteBatch batch;

    private final Map map;

    public CampaignGameScreen(TowerDefense game) {
        this.game = game;
        this.map = new Map(Gdx.files.internal("maps/map_1.txt"));


    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        batch.begin();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        map.draw(batch, 0);

        batch.end();
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
            int y = screenX / 64;
            int x = screenY / 64;
            map.toggleTile(x, y);
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
