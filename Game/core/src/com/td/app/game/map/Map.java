package com.td.app.game.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.io.BufferedReader;
import java.io.FileReader;

public class Map extends Actor {

    private final Tile[][] map;

    public Map() {
        this.map = createRandomMap();
    }

    // FileHandle : Gdx.files.internal("./maps/map_*.txt")
    public Map(FileHandle fileHandle) {
        Tile[][] map1;
        map1 = createMapFromFile(fileHandle);

        if(map1 == null){
            map1 = createRandomMap();
        }
        map = map1;
    }

    // TODO
    private Tile[][] createRandomMap(){
        return null;
    }

    private Tile[][] createMapFromFile(FileHandle fileHandle) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(fileHandle)));

            int mapLength = Integer.parseInt(reader.readLine());
            int mapWidth = Integer.parseInt(reader.readLine());

            Tile[][] map = new Tile[mapLength][mapWidth];
            String line;

            for (int i = 0; i < mapLength; i++) {
                line = reader.readLine();
                Tile[] tileArray = new Tile[mapWidth];

                String[] splits = line.split(" ");

                for (int j = 0; j < mapWidth; j++) {
                    int tileType = Integer.parseInt(splits[j]);
                    tileArray[j] = new Tile(MapElements.values()[tileType]);
                }
                map[i] = tileArray;
            }

            reader.close();
            return map;
        }
        catch (Exception e){
            System.out.println("Unable to read file");
            return null;
        }
    }

    public Tile[][] getMap() {
        return map;
    }
}
