package com.td.app.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScreenButtonTexture extends Actor {
    private Texture texture;
    private final Sprite sprite;
    private final ButtonType type;

    /**
     * Enums all button types used for a specific screen button
     */
    public enum ButtonType { ARCADE, CAMPAIGN, MUSICON, MUSICOFF, NEWGAME, RETURN, SETTINGS,
        SOUNDON, SOUNDOFF, SPEED_CONTROLLER
    }

    /**
     * <p>
     *     Creates a button with a texture
     * </p>
     * <p>
     *     The button's type is handled by {@link ButtonType}
     * </p>
     * @param imgPath the path to the texture's location
     * @param type the button's type
     */
    public ScreenButtonTexture(String imgPath, ButtonType type) {
        this.type = type;
        texture = new Texture(Gdx.files.internal(imgPath));
        setBounds(0, 0, texture.getWidth(), texture.getHeight());
        sprite = new Sprite(texture);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1 ,0);
        super.draw(batch, parentAlpha);
    }

    public ButtonType getType() {
        return type;
    }
}
