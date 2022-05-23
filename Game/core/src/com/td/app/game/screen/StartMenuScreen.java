package com.td.app.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.td.app.TowerDefense;
import com.td.app.game.gui.PointerTexture;
import com.td.app.game.gui.ScreenButtonTexture;

import java.util.Iterator;

public class StartMenuScreen implements Screen, InputProcessor {
    public TowerDefense game;
    private Stage stage;
    private Image backgroundImage;
    private ScreenButtonTexture arcadeButton;
    private ScreenButtonTexture campaignButton;
    private ScreenButtonTexture settingsButton;
    private ScreenButtonTexture backButton;
    private PointerTexture pointer;

    public StartMenuScreen(TowerDefense game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (stage == null) {
            if (TowerDefense.pref.getBoolean("music")) {
                game.music.play();
            }
            stage = new Stage();

            backgroundImage = new Image(new Texture(Gdx.files.internal("textures/menu/startMenu.jpg")));
            backgroundImage.setPosition(0, 0);

            arcadeButton = new ScreenButtonTexture("textures/button/arcadeButton.png", ScreenButtonTexture.ButtonType.ARCADE);
            arcadeButton.setPosition(stage.getWidth() / 1.42F, stage.getHeight() / 2.11F);

            campaignButton = new ScreenButtonTexture("textures/button/campaignButton.png", ScreenButtonTexture.ButtonType.CAMPAIGN);
            campaignButton.setPosition(stage.getWidth() / 10.66F, stage.getHeight() / 2.18F);

            settingsButton = new ScreenButtonTexture("textures/button/settingsButton.png", ScreenButtonTexture.ButtonType.SETTINGS);
            settingsButton.setPosition(stage.getWidth() - stage.getWidth() / 200 - settingsButton.getWidth(), stage.getHeight() / 200);

            backButton = new ScreenButtonTexture("textures/button/backButton.png", ScreenButtonTexture.ButtonType.RETURN);
            backButton.setPosition(stage.getWidth() / 200, stage.getHeight() / 200);

            pointer = new PointerTexture("textures/menu/pointer.png", campaignButton);
            pointer.setSize(pointer.getWidth() * 0.7F, pointer.getHeight() * 0.7F);
            pointer.setPosition(campaignButton.getX() - 95, campaignButton.getY() + campaignButton.getHeight() / 2 - pointer.getHeight() / 2);

            stage.addActor(backgroundImage);
            stage.addActor(arcadeButton);
            stage.addActor(campaignButton);
            stage.addActor(settingsButton);
            stage.addActor(backButton);
            stage.addActor(pointer);
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
        if (keycode == Input.Keys.DPAD_DOWN) {
            if (pointer.getPointedButton().getType() == ScreenButtonTexture.ButtonType.ARCADE) {
                pointer.setPointedButton(campaignButton);
                pointer.setPosition(campaignButton.getX() - 95, campaignButton.getY() + campaignButton.getHeight()/2 - pointer.getHeight()/2);
            } else if (pointer.getPointedButton().getType() == ScreenButtonTexture.ButtonType.CAMPAIGN) {
                pointer.setPointedButton(settingsButton);
                pointer.setPosition(settingsButton.getX() - 95, settingsButton.getY() + settingsButton.getHeight()/2 - pointer.getHeight()/2);
            } else if (pointer.getPointedButton().getType() == ScreenButtonTexture.ButtonType.SETTINGS) {
                pointer.setPointedButton(arcadeButton);
                pointer.setPosition(arcadeButton.getX() - 95, arcadeButton.getY() + arcadeButton.getHeight()/2 - pointer.getHeight()/2);
            }

        } else if (keycode == Input.Keys.DPAD_UP) {
            if (pointer.getPointedButton().getType() == ScreenButtonTexture.ButtonType.ARCADE) {
                pointer.setPointedButton(settingsButton);
                pointer.setPosition(settingsButton.getX() - 95, settingsButton.getY() + settingsButton.getHeight()/2 - pointer.getHeight()/2);
            } else if (pointer.getPointedButton().getType() == ScreenButtonTexture.ButtonType.CAMPAIGN) {
                pointer.setPointedButton(arcadeButton);
                pointer.setPosition(arcadeButton.getX() - 95, arcadeButton.getY() + arcadeButton.getHeight()/2 - pointer.getHeight()/2);
            } else if (pointer.getPointedButton().getType() == ScreenButtonTexture.ButtonType.SETTINGS) {
                pointer.setPointedButton(campaignButton);
                pointer.setPosition(campaignButton.getX() - 95, campaignButton.getY() + campaignButton.getHeight()/2 - pointer.getHeight()/2);
            }

        } else if (keycode == Input.Keys.ENTER) {
            if (pointer.getPointedButton().getType() == ScreenButtonTexture.ButtonType.ARCADE) {
                Gdx.input.setInputProcessor(null);
                game.toArcadeMenuScreen();
                dispose();
            } else if (pointer.getPointedButton().getType() == ScreenButtonTexture.ButtonType.CAMPAIGN) {
                Gdx.input.setInputProcessor(null);
                game.toCampaignMenuScreen();
                dispose();
            } else if (pointer.getPointedButton().getType() == ScreenButtonTexture.ButtonType.SETTINGS) {
                Gdx.input.setInputProcessor(null);
                game.toSettingsScreen();
                dispose();
            }
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
    public boolean touchUp(int screenX, int screenY, int pointer, final int button) {
        Vector2 hover = stage.screenToStageCoordinates(new Vector2(screenX,screenY));
        Actor actor = stage.hit(hover.x,hover.y,true);

        if (actor instanceof ScreenButtonTexture) {
            ScreenButtonTexture screenButton = (ScreenButtonTexture) actor;
            if (screenButton.getType() == ScreenButtonTexture.ButtonType.RETURN) {
                Gdx.input.setInputProcessor(stage);
                new Dialog("Quit the game", new Skin(Gdx.files.internal("skin/uiskin.json"))) {
                    {
                        text("Are you sure you want to quit ?");
                        button("Yes", "Yes");
                        button("No", "No");
                    }
                    @Override
                    protected void result(Object object) {
                        if (object.equals("Yes")) {
                            Gdx.app.exit();
                        } else {
                            Iterator<Actor> it = stage.getActors().iterator();
                            while (it.hasNext()) {
                                if (it.next().equals(this)) {
                                    it.remove();
                                    break;
                                }
                            }
                            StartMenuScreen.this.show();
                        }
                    }
                }.show(stage);
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.ARCADE) {
                Gdx.input.setInputProcessor(null);
                game.toArcadeMenuScreen();
                dispose();
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.CAMPAIGN) {
                Gdx.input.setInputProcessor(null);
                game.toCampaignMenuScreen();
                dispose();
            } else if (screenButton.getType() == ScreenButtonTexture.ButtonType.SETTINGS) {
                Gdx.input.setInputProcessor(null);
                game.toSettingsScreen();
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
