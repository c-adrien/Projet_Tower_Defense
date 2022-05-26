package com.td.app.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class CampaignLevel extends Image {
    private final int level;
    private boolean isLocked;

    public CampaignLevel(String imgPath, int level) {
        super(new Texture(Gdx.files.internal(imgPath)));
        this.level = level;
        isLocked = true;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void unlockLevel() {
        isLocked = false;
    }
}
