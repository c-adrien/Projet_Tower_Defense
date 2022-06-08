package com.td.app.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Arrays;
import java.util.List;

public class Tile {

    private static final List<MapElements> notOccupiedElements = Arrays.asList(MapElements.DALLE);

    public final MapElements mapElement;
    private boolean isOccupied;
    private boolean isSelected;

    private final Texture texture;
    private Texture alternativeTexture;

    //==============================================

    public Tile(MapElements mapElement) {
        this.mapElement = mapElement;

        isOccupied = !notOccupiedElements.contains(mapElement);

        isSelected = false;

        texture = new Texture(Gdx.files.internal(String.format(
                "textures/landscape/%s.png", mapElement.name())));
        try {
            alternativeTexture = new Texture(Gdx.files.internal(String.format(
                    "textures/landscape/%s_alt.png", mapElement.name())));
        }
        catch (Exception e){
            alternativeTexture = null;
        }

    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public Texture getTexture() {
        if(isSelected && alternativeTexture != null && !isOccupied){
            return alternativeTexture;
        }
        return texture;
    }

    public void select(){
        isSelected = true;
    }

    public void unselect(){
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
