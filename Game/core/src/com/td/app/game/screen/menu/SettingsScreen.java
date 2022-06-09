package com.td.app.game.screen.menu;

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
import com.td.app.Helper;
import com.td.app.SoundHandler;
import com.td.app.TowerDefense;
import com.td.app.game.gui.ScreenButtonTexture;

public class SettingsScreen implements Screen, InputProcessor {
    public TowerDefense game;
    private Stage stage;
    private Image background;
    private Image musicTexture;
    private Image soundTexture;
    private ScreenButtonTexture musicDisplay;
    private ScreenButtonTexture soundDisplay;
    private ScreenButtonTexture backButton;
    private ScreenButtonTexture newGameButton;

    /**
     * Represents the menu used for the settings
     * @param game the game's screen handler
     */
    public SettingsScreen(TowerDefense game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        background = new Image(new Texture(Gdx.files.internal("textures/menu/settingsMenu.png")));

        musicTexture = new Image(new Texture(Gdx.files.internal("textures/menu/musicTexture.png")));
        musicTexture.setPosition(stage.getWidth() * 0.3F, stage.getHeight() * 0.6F);

        soundTexture = new Image(new Texture(Gdx.files.internal("textures/menu/soundTexture.png")));
        soundTexture.setPosition(stage.getWidth() * 0.3F, stage.getHeight() * 0.36F);

        if (TowerDefense.pref.getBoolean("music")) {
            musicDisplay = new ScreenButtonTexture("textures/button/soundOn.png", ScreenButtonTexture.ButtonType.MUSICON);
        } else {
            musicDisplay = new ScreenButtonTexture("textures/button/soundOff.png", ScreenButtonTexture.ButtonType.MUSICOFF);
        }
        if (TowerDefense.pref.getBoolean("sound")) {
            soundDisplay = new ScreenButtonTexture("textures/button/soundOn.png", ScreenButtonTexture.ButtonType.SOUNDON);
        } else {
            soundDisplay = new ScreenButtonTexture("textures/button/soundOff.png", ScreenButtonTexture.ButtonType.SOUNDOFF);
        }
        musicDisplay.setPosition(stage.getWidth() * 0.6F, stage.getHeight() * 0.58F);
        soundDisplay.setPosition(stage.getWidth() * 0.6F, stage.getHeight() * 0.34F);

        backButton = new ScreenButtonTexture("textures/button/backButton.png", ScreenButtonTexture.ButtonType.RETURN);
        backButton.setPosition(stage.getWidth() / 200, stage.getHeight() / 200);

        newGameButton = new ScreenButtonTexture("textures/button/newGameButton.png", ScreenButtonTexture.ButtonType.NEWGAME);
        newGameButton.setPosition(stage.getWidth() * 0.38F, stage.getHeight() * 0.1F);

        stage.addActor(background);
        stage.addActor(musicTexture);
        stage.addActor(soundTexture);
        stage.addActor(musicDisplay);
        stage.addActor(soundDisplay);
        stage.addActor(backButton);
        stage.addActor(newGameButton);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 hover = stage.screenToStageCoordinates(new Vector2(screenX,screenY));
        Actor actor = stage.hit(hover.x,hover.y,true);

        if (actor instanceof ScreenButtonTexture) {
            ScreenButtonTexture screenButton = (ScreenButtonTexture) actor;
            if (screenButton.getType() == ScreenButtonTexture.ButtonType.RETURN) { // Go back to previous screen (StartMenu)
                SoundHandler.play("click");
                Gdx.input.setInputProcessor(null);
                game.toStartMenu();
                dispose();
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.MUSICON) { // Desactivate music
                SoundHandler.play("click");
                Helper.removeActorFromStage(musicDisplay, stage);

                musicDisplay = new ScreenButtonTexture("textures/button/soundOff.png", ScreenButtonTexture.ButtonType.MUSICOFF);
                musicDisplay.setPosition(stage.getWidth() * 0.6F, stage.getHeight() * 0.58F);
                stage.addActor(musicDisplay);

                TowerDefense.music.stop();
                TowerDefense.pref.putBoolean("music", false);
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.MUSICOFF) { // Activate music
                SoundHandler.play("click");
                Helper.removeActorFromStage(musicDisplay, stage);

                musicDisplay = new ScreenButtonTexture("textures/button/soundOn.png", ScreenButtonTexture.ButtonType.MUSICON);
                musicDisplay.setPosition(stage.getWidth() * 0.6F, stage.getHeight() * 0.58F);
                stage.addActor(musicDisplay);

                TowerDefense.music.play();
                TowerDefense.pref.putBoolean("music", true);
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.SOUNDON) { // Desactivate sound
                SoundHandler.play("click");
                Helper.removeActorFromStage(soundDisplay, stage);

                soundDisplay = new ScreenButtonTexture("textures/button/soundOff.png", ScreenButtonTexture.ButtonType.SOUNDOFF);
                soundDisplay.setPosition(stage.getWidth() * 0.6F, stage.getHeight() * 0.34F);
                stage.addActor(soundDisplay);

                SoundHandler.pauseAll();
                TowerDefense.pref.putBoolean("sound", false);
                TowerDefense.pref.flush();
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.SOUNDOFF) { // Activate sound
                Helper.removeActorFromStage(soundDisplay, stage);
                SoundHandler.play("click");

                soundDisplay = new ScreenButtonTexture("textures/button/soundOn.png", ScreenButtonTexture.ButtonType.SOUNDON);
                soundDisplay.setPosition(stage.getWidth() * 0.6F, stage.getHeight() * 0.34F);
                stage.addActor(soundDisplay);

                SoundHandler.playAll();
                TowerDefense.pref.putBoolean("sound", true);
                TowerDefense.pref.flush();
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.NEWGAME) { // Start new game
                SoundHandler.play("click");
                Gdx.input.setInputProcessor(null);
                TowerDefense.music.stop();
                game.newGame();
            }
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) { // Go back to previous menu (StartMenu)
            Gdx.input.setInputProcessor(null);
            game.toStartMenu();
            dispose();
            return true;
        }
        return false;
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
