package com.td.app.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ShopButton extends Image {
    private final ShopButtonType type;

    /**
     * Enums all button types used for a specific shop button
     */
    public enum ShopButtonType { SELL, UPGRADE }

    /**
     * <p>
     *     Creates a button with a texture used for the in-game shop
     * </p>
     * <p>
     *     The button's type is handle by {@link ShopButtonType}
     * </p>
     * @param imgPath the path to the texture's location
     * @param type the button type
     */
    public ShopButton(String imgPath, ShopButtonType type) {
        super(new Texture(Gdx.files.internal(imgPath)));
        this.type = type;
    }

    public ShopButtonType getType() {
        return type;
    }
}
