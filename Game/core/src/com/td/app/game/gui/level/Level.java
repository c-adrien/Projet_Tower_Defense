package com.td.app.game.gui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Level extends Image {
    private final int LEVEL;

    /**
     * Creates a texture for the level selection (e.g. for campaign and arcade levels)
     * @param imgPath the path to the texture's location
     * @param level the level's number
     */
    public Level(String imgPath, int level) {
        super(new Texture(Gdx.files.internal(imgPath)));
        this.LEVEL = level;
    }

    public int getLEVEL() {
        return LEVEL;
    }
}
