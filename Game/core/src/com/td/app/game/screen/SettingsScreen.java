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

import java.util.Iterator;

public class SettingsScreen implements Screen, InputProcessor {
    public TowerDefense game;
    private Stage stage;
    private Image background;
    private Image musicTexture;
    private ScreenButtonTexture musicDisplay;
    private ScreenButtonTexture backButton;
    private ScreenButtonTexture newGameButton;

    public SettingsScreen(TowerDefense game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (stage == null) {
            stage = new Stage();
            background = new Image(new Texture(Gdx.files.internal("textures/menu/settingsMenu.png")));

            musicTexture = new Image(new Texture(Gdx.files.internal("textures/menu/musicTexture.png")));
            musicTexture.setPosition(stage.getWidth() * 0.3F, stage.getHeight() * 0.6F);

            if (TowerDefense.pref.getBoolean("music")) {
                musicDisplay = new ScreenButtonTexture("textures/button/musicOn.png", ScreenButtonTexture.ButtonType.MUSICON);
            } else {
                musicDisplay = new ScreenButtonTexture("textures/button/musicOff.png", ScreenButtonTexture.ButtonType.MUSICOFF);
            }
            musicDisplay.setPosition(stage.getWidth() * 0.6F, stage.getHeight() * 0.58F);

            backButton = new ScreenButtonTexture("textures/button/backButton.png", ScreenButtonTexture.ButtonType.RETURN);
            backButton.setPosition(stage.getWidth() / 200, stage.getHeight() / 200);

            newGameButton = new ScreenButtonTexture("textures/button/newGameButton.png", ScreenButtonTexture.ButtonType.NEWGAME);
            newGameButton.setPosition(stage.getWidth() * 0.4F, stage.getHeight() * 0.1F);

            stage.addActor(background);
            stage.addActor(musicTexture);
            stage.addActor(musicDisplay);
            stage.addActor(backButton);
            stage.addActor(newGameButton);
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
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.MUSICON) {
                Iterator<Actor> it = stage.getActors().iterator();
                while (it.hasNext()) {
                    if (it.next().equals(musicDisplay)) {
                        it.remove();
                        musicDisplay = new ScreenButtonTexture("textures/button/musicOff.png", ScreenButtonTexture.ButtonType.MUSICOFF);
                        musicDisplay.setPosition(screenButton.getWidth(), screenButton.getHeight());
                        stage.addActor(musicDisplay);
                        break;
                    }
                }
                TowerDefense.music.stop();
                TowerDefense.pref.putBoolean("music", false);
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.MUSICOFF) {
                Iterator<Actor> it = stage.getActors().iterator();
                while (it.hasNext()) {
                    if (it.next().equals(musicDisplay)) {
                        it.remove();
                        musicDisplay = new ScreenButtonTexture("textures/button/musicOn.png", ScreenButtonTexture.ButtonType.MUSICON);
                        musicDisplay.setPosition(screenButton.getWidth(), screenButton.getHeight());
                        stage.addActor(musicDisplay);
                        break;
                    }
                }
                TowerDefense.music.play();
                TowerDefense.pref.putBoolean("music", true);
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.NEWGAME) {
                Gdx.input.setInputProcessor(null);
                TowerDefense.music.stop();
                game.newGame();
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
