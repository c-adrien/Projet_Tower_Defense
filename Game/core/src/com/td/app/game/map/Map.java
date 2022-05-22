package com.td.app.game.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.io.BufferedReader;
import java.io.File;
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
            File file = new File("./assets/"+fileHandle.path());
            BufferedReader reader = new BufferedReader(new FileReader(file));

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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int size = 64;
        int nbTiles = 12;

        setBounds(0, 0, nbTiles*64, nbTiles*64);

        for (int i = 0; i < nbTiles; i++) {
            for (int j = 0; j < nbTiles; j++) {
                Texture texture = map[nbTiles-1-j][i].getTexture();
                Sprite sprite = new Sprite(texture);

                batch.draw(sprite, size*i, size*j, size*i, size*i, size, size,
                        1, 1,0);
                super.draw(batch, parentAlpha);
            }
        }
    }
}
