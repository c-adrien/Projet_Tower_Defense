package com.td.app.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ShopButton extends Image {
    public enum ShopButtonType { SELL, UPGRADE }

    private final ShopButtonType type;

    public ShopButton(String imgPath, ShopButtonType type) {
        super(new Texture(Gdx.files.internal(imgPath)));
        this.type = type;
    }

    public ShopButtonType getType() {
        return type;
    }
}
