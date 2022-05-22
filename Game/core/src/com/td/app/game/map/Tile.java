package com.td.app.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Arrays;
import java.util.List;

public class Tile extends Actor {

    private static final List<MapElements> notOccupiedElements = Arrays.asList(MapElements.DALLE);

    private final MapElements mapElement;
    private boolean isOccupied;

    private Texture texture;

    //==============================================

    public Tile(MapElements mapElement) {
        this.mapElement = mapElement;

        if (notOccupiedElements.contains(mapElement)){
            isOccupied = true;
        }

        texture = new Texture(Gdx.files.internal(String.format(
                "./textures/landscape/%s.png", mapElement.name())));
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public Texture getTexture() {
        return texture;
    }
}
