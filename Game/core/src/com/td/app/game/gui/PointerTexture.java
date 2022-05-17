package com.td.app.game.gui;

public class PointerTexture extends ScreenButtonTexture {
    private ScreenButtonTexture pointedButton;

    public PointerTexture(String imgPath, ButtonType type, ScreenButtonTexture pointedButton) {
        super(imgPath, type);
        this.pointedButton = pointedButton;
    }

    public ScreenButtonTexture getPointedButton() {
        return pointedButton;
    }

    public void setPointedButton(ScreenButtonTexture pointedButton) {
        this.pointedButton = pointedButton;
    }
}
