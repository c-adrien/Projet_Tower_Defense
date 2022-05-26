package com.td.app.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.td.app.TowerDefense;

public class NewUserScreen implements Screen {
    private final TowerDefense game;
    private Stage stage;
    private TextField textField;
    private TextButton textButton;

    public NewUserScreen(TowerDefense game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (stage == null) {
            stage = new Stage();
            Image newUserTexture = new Image(new Texture(Gdx.files.internal("textures/menu/newUserWelcomeMsgTexture.png")));
            newUserTexture.setPosition(stage.getWidth() * 0.25F, stage.getHeight() * 0.5F);

            textField = new TextField("", new Skin(Gdx.files.internal("skin/uiskin.json")));
            textField.setMaxLength(10);
            textButton = new TextButton("Let's go!", new Skin(Gdx.files.internal("skin/uiskin.json")));

            textButton.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (!textField.getText().isEmpty()) {
                        buttonClicked();
                    }
                }
            });

            textField.setSize(stage.getWidth() * 0.5F, 50);
            textField.setPosition(stage.getWidth() * 0.25F, stage.getHeight() / 2);
            textButton.setSize(stage.getWidth() * 0.5F, 50);
            textButton.setPosition(stage.getWidth() * 0.25F, stage.getHeight() / 2 - 100);

            stage.addActor(newUserTexture);
            stage.addActor(textField);
            stage.addActor(textButton);
        }
        Gdx.input.setInputProcessor(stage);
    }

    private void buttonClicked() {
        TowerDefense.pref.putString("user", textField.getText());
        TowerDefense.pref.flush();
        game.toStartMenu();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        if (textButton.getClickListener().isPressed()) {
            if (!textField.getText().isEmpty()) {
                buttonClicked();
            }
        }
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
}
