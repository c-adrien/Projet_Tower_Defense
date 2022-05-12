package com.td.app.game.map;

public class Tile {

    private final MapElements mapElement;
    private boolean isOccupied;

    //==============================================

    public Tile(MapElements mapElement) {
        this.mapElement = mapElement;

        if (!mapElement.equals(MapElements.DALLE)){
            isOccupied = true;
        }
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}
