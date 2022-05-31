package com.td.app.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.td.app.TowerDefense;
import com.td.app.game.gui.ScreenButtonTexture;
import com.td.app.game.gui.CampaignLevel;

public class CampaignMenuScreen implements Screen, InputProcessor {
    public TowerDefense game;
    private Stage stage;
    private Image background;
    private ScreenButtonTexture backButton;
    private final int NUMBER_OF_LEVEL = 10;

    public CampaignMenuScreen(TowerDefense game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();

        background = new Image(new Texture(Gdx.files.internal("textures/menu/campaignMenu.png")));
        backButton = new ScreenButtonTexture("textures/button/backButton.png", ScreenButtonTexture.ButtonType.RETURN);
        backButton.setPosition(stage.getWidth() / 200, stage.getHeight() / 200);

        stage.addActor(background);
        stage.addActor(backButton);

        for (int i = 0; i < TowerDefense.pref.getInteger("unlockedLevels"); i++) {
            CampaignLevel levelButton = new CampaignLevel(String.format("textures/level/level%s.png", i + 1), i + 1);
            levelButton.unlockLevel();
            levelButton.setPosition(((i % 5) * stage.getWidth() / 5) + 75, (float) ((2 - Math.floor(i/5F)) * stage.getHeight() / 4));
            stage.addActor(levelButton);
        }
        for (int i = TowerDefense.pref.getInteger("unlockedLevels"); i < NUMBER_OF_LEVEL; i++) {
            CampaignLevel levelButton = new CampaignLevel("textures/level/lock.png", i + 1);
            levelButton.setPosition(((i % 5) * stage.getWidth() / 5) + 75, (float) ((2 - Math.floor(i/5F)) * stage.getHeight() / 4));
            stage.addActor(levelButton);
        }

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
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

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.input.setInputProcessor(null);
            game.toStartMenu();
            dispose();
            return true;
        }

        // TODO change
        if (keycode == Input.Keys.ENTER){
            Gdx.input.setInputProcessor(null);
            game.toCampaignGameScreen(1);
            dispose();
            return true;
        }
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 hover = stage.screenToStageCoordinates(new Vector2(screenX,screenY));
        Actor actor = stage.hit(hover.x,hover.y,true);

        if (actor instanceof ScreenButtonTexture) {
            ScreenButtonTexture screenButton = (ScreenButtonTexture) actor;
            if (screenButton.getType() == ScreenButtonTexture.ButtonType.RETURN) {
                Gdx.input.setInputProcessor(null);
                game.toStartMenu();
                dispose();
            }
        }
        if (actor instanceof CampaignLevel) {
            CampaignLevel level = (CampaignLevel) actor;
            if (!level.isLocked()) {
                Gdx.input.setInputProcessor(null);
                game.toCampaignGameScreen(level.getLevel());
                dispose();
            }
        }
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
