package com.td.app.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Collections;
import java.util.List;

public class Tile {
    private static final List<MapElements> notOccupiedElements = Collections.singletonList(MapElements.DALLE);

    public final MapElements mapElement;
    private boolean isOccupied;
    private boolean isSelected;

    private final Texture texture;
    private Texture alternativeTexture;

    /**
     * <p>
     *     Creates a tile with texture used in the map
     * </p>
     * <p>
     *     The tile's element is handled by {@link MapElements}
     * </p>
     * @param mapElement the tile's element
     */
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

    /**
     * <p>
     *     Gets the tile's texture
     * </p>
     * <p>
     *     Tile's texture differs when the tile is selected or not
     * </p>
     * @return the current tile's texture
     */
    public Texture getTexture() {
        if (isSelected && alternativeTexture != null && !isOccupied) {
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
