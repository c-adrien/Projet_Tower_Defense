package com.td.app.game.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Map extends Actor {

    private final Tile[][] map;

    private static final int size = 64;
    private static final int nbTiles = 12;

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

        /** FILLING MAP WITH DALLE
         * (0,0) at top corner left
         */
        Tile[][] map = new Tile[nbTiles][nbTiles];

        for (int i = 0; i < nbTiles; i++) {
            for (int j = 0; j < nbTiles; j++) {
                if (map[i][j] == null){
                    map[i][j] = new Tile(MapElements.DALLE);
                    System.out.println(map[i][j].getTexture().toString().contains("DALLE"));
                }
            }
        }


        /** CREATING START OF PATH */
        boolean isStartCreated = false;
        int i = 0;
        int j = 0;

        if (!isStartCreated){
            int RANDOM_START = new Random().nextInt(12);
            System.out.println("RANDOM START " + RANDOM_START);
            i = RANDOM_START;
//            map[i][j] = new Tile(MapElements.CHEMIN_HORIZONTAL);
            map[i][j] = new Tile(MapElements.BUISSON);
//            map[i][j] = new Tile(MapElements.values()[0]);
//            System.out.println(" is null " + map[i][j].getTexture() == null);
//            System.out.println(map[i][j].getTexture().toString().contains("CHEMIN_HORIZONTAL"));

            isStartCreated = true;
        }

        /** CREATING REST OF PATH */
        int up = 0;
        int right = 1;
        int down = 2;

        while(j<nbTiles-1){
            int direction = new Random().nextInt(3);
            System.out.println(" i,j = " + i + "," + j + " | DIRECTION = " + direction);

            if (direction == right){
                j++;
            }
//            else if(direction == up){
//                i--;
//            }
//            else if(direction == down){
//                i++;
//            }
            map[i][j] = new Tile(MapElements.CHEMIN_HORIZONTAL);

        }

        /** FILLING MAP WITH LANDSCAPE ELEMENTS */
        int nbOfElt = 0;
        for (int k = 0; k < nbTiles-1; k++) {
            for (int l = 0; l < nbTiles-1; l++) {

                if (nbOfElt == 20){ // max of 20 elt on the map
                    break;
                }

                double toFill = Math.random();
                System.out.println("toFill + " + toFill);
                if (toFill>0.2){
                    continue;
                }
                else if (toFill<0.2){
                    if (map[k][l].getTexture().toString().contains("DALLE")){
                        int eltChoice = new Random().nextInt(6);
                        map[k][l] = new Tile(MapElements.values()[eltChoice]);
//                        map[k][l] = new Tile(MapElements.BUISSON);
                        nbOfElt++;
                    }
                }
                System.out.println(map[i][j].getTexture().toString().contains("DALLE"));
            }
        }

        return map;
    }



    private Tile[][] createMapFromFile(FileHandle fileHandle) {
        try {
            BufferedReader reader = getBufferedReader(fileHandle);

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
            e.printStackTrace();
            return null;
        }
    }

    private BufferedReader getBufferedReader(FileHandle fileHandle){

        List<String> patterns = Arrays.asList("Game/assets/", "./assets/");

        for (String pattern: patterns) {
            try {
                File file = new File(pattern+ fileHandle.path());
                return new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ignored) {}
        }
        return null;
    }

    public Tile[][] getMap() {
        return map;
    }

    public Tile getTileFromPosition(int x, int y){
        x = x / 64;
        y = y / 64;

        int line = y;
        int column = x;

        Tile tile = map[line][column];
        return tile;
    }

    public void toggleTile(Tile tile){
        if(tile.isSelected()){
            tile.unselect();
            return;
        }

        for (int i = 0; i < nbTiles; i++) {
            for (int j = 0; j < nbTiles; j++) {
                if (!map[i][j].equals(tile)) {
                    map[i][j].unselect();
                }
            }
        }
        tile.select();
    }

    public void toggleTile(int x, int y){
        if(map[x][y].isSelected()){
            map[x][y].unselect();
            return;
        }

        for (int i = 0; i < nbTiles; i++) {
            for (int j = 0; j < nbTiles; j++) {
                map[i][j].unselect();
            }
        }
        map[x][y].select();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
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
