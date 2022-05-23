package com.td.app.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
public class PointerTexture extends Image {
    private ScreenButtonTexture pointedButton;
    public PointerTexture(String imgPath, ScreenButtonTexture pointedButton) {
        super(new Texture(Gdx.files.internal(imgPath)));
        this.pointedButton = pointedButton;
    }
    public ScreenButtonTexture getPointedButton() {
        return pointedButton;
    }
    public void setPointedButton(ScreenButtonTexture pointedButton) {
        this.pointedButton = pointedButton;
    }
}
