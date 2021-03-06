package com.td.app.game.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.game.Position;

import java.io.BufferedReader;
import java.util.Random;

public class Map extends Actor {
    private static final Random random = new Random();

    private final Tile[][] map;
    protected Position entryTilePosition;

    private static final int size = 64;
    private static final int nbTiles = 12;
    public static final int TOTAL_SIZE = size * nbTiles;

    /**
     * Creates a random map
     */
    public Map() {
        this.map = createRandomMap();
        this.entryTilePosition = findEntryTilePosition();
    }

    /**
     * <p>
     *    Creates a map from a file
     * </p>
     * <p>
     *     If the file is not found, creates a random map instead
     * </p>
     * @param fileHandle the file's location
     */
    public Map(FileHandle fileHandle) {
        Tile[][] map1;
        if ((map1 = createMapFromFile(fileHandle)) == null) {
            map1 = createRandomMap();
        }
        map = map1;

        this.entryTilePosition = findEntryTilePosition();
    }

    /**
     * <p>
     *     Creates a random map
     * </p>
     * <p>
     *     The path always starts on the left and goes from left to right with some up and down pathing
     * </p>
     * <p>
     *     A path's part can never be at the very top nor very bottom of the map
     * </p>
     * @return the random map created
     */
    private Tile[][] createRandomMap() {
        /* FILLS MAP WITH DALLE
         * (0,0) at top left corner
         */
        Tile[][] map = new Tile[nbTiles][nbTiles];

        for (int i = 0; i < nbTiles; i++) {
            for (int j = 0; j < nbTiles; j++) {
                if (map[i][j] == null) {
                    map[i][j] = new Tile(MapElements.DALLE);
                }
            }
        }

        /* CREATES START OF PATH
         * Margin to start NOR at the very top NEITHER at the very bottom of the map
         */
        int i = new Random().nextInt(6)+3;
        int j = 0;

        map[i][j] = new Tile(MapElements.CHEMIN_HORIZONTAL);

        // CREATES REST OF PATH
        int up = 0; // i--
        int right = 1; // j++
        int down = 2; // i++

        int previousDirection = -1;

        while (j < nbTiles - 1) {
            int direction = random.nextInt(3);
            switch (previousDirection) { // Seeks for new direction when conflicts with opposite direction (e.g up & down)
                case -1:
                    previousDirection = direction;
                    break;
                case 0:
                    while (direction == 2) {
                        direction = random.nextInt(3);
                    }
                    previousDirection = direction;
                    break;
                case 2:
                    while (direction == 0) {
                        direction = random.nextInt(3);
                    }
                    previousDirection = direction;
                    break;
            }

            // RIGHT ???
            if (direction == right) {
                j++; // ???
                map[i][j] = new Tile(MapElements.CHEMIN_HORIZONTAL);

                // ADAPTS PREVIOUS TILE TO ACTUAL TILE
                if (!map[i][j-1].getTexture().toString().contains("DALLE")) { // Previous tile
                    if (!map[i-1][j-1].getTexture().toString().contains("DALLE")) { // Previous previous tile
                        map[i][j-1] = new Tile(MapElements.CHEMIN_HAUT_DROITE);
                    }
                    else if (!map[i+1][j-1].getTexture().toString().contains("DALLE")) { // Previous previous tile
                        map[i][j-1] = new Tile(MapElements.CHEMIN_BAS_DROITE);
                    }
               }
            }

            /* TURN ??????
             * UP
             */
            if (direction == up && i > 1) {
                if (map[i-1][j].getTexture().toString().contains("DALLE")) {
                    i--; // ???
                    map[i][j] = new Tile(MapElements.CHEMIN_VERTICAL);

                    // ADAPTS PREVIOUS TILE TO ACTUAL TILE
                    if (map[i + 1][j].getTexture().toString().contains("CHEMIN_HORIZONTAL")) { // previous tile
                        map[i + 1][j] = new Tile(MapElements.CHEMIN_GAUCHE_HAUT);
                    }

                    int nbVertical = new Random().nextInt(nbTiles) - i;
                    for (int k = 0; k <  nbVertical; k++) {
                        if (i <= 1) {
                            map[i][j] = new Tile(MapElements.CHEMIN_BAS_DROITE);
                            break;
                        }
                        i--;
                        map[i][j] = new Tile(MapElements.CHEMIN_VERTICAL);
                    }
                }
            }

            // DOWN
            else if(direction == down && i < nbTiles - 2) {
                if (map[i+1][j].getTexture().toString().contains("DALLE")) {
                    i++; // ???
                    map[i][j] = new Tile(MapElements.CHEMIN_VERTICAL);

                    // ADAPTS PREVIOUS TILE TO ACTUAL TILE
                    if (map[i-1][j].getTexture().toString().contains("CHEMIN_HORIZONTAL")) { // previous tile
                        map[i-1][j] = new Tile(MapElements.CHEMIN_GAUCHE_BAS);
                    }

                    int nbVertical = new Random().nextInt(nbTiles) - i;
                    for (int k = 0; k <  nbVertical; k++) {
                        if (i >= nbTiles - 2) {
                            map[i][j] = new Tile(MapElements.CHEMIN_HAUT_DROITE);
                            break;
                        }
                        i++;
                        map[i][j] = new Tile(MapElements.CHEMIN_VERTICAL);
                    }
                }
            }
        }

        // FILLS REST OF MAP WITH LANDSCAPE ELEMENTS
        int nbOfElt = 0;
        int nbOfEltMax = 35;

        for (int k = 0; k < nbTiles-1; k++) {
            for (int l = 0; l < nbTiles-1; l++) {
                if (nbOfElt == nbOfEltMax) {
                    break;
                }

                double toFill = random.nextDouble();
                double chanceOfElt = 0.10;
                double chanceOfGrass = 0.90;
                if (map[k][l].getTexture().toString().contains("DALLE")) {
                    if (toFill > chanceOfGrass) {
                        map[k][l] = new Tile(MapElements.DALLE2);
                    }
                    else if (toFill < chanceOfElt) { // Distributing evenly elements through the map
                        if (map[k][l].getTexture().toString().contains("DALLE")) {
                            int eltChoice = new Random().nextInt(11)+1;
                            map[k][l] = new Tile(MapElements.values()[eltChoice]);
                            nbOfElt++;
                        }
                    }
                }
            }
        }

        return map;
    }

    /**
     * Creates a map from a file
     * @param fileHandle the file's location
     * @return the map created
     */
    private Tile[][] createMapFromFile(FileHandle fileHandle) {
        try {
            BufferedReader reader = fileHandle.reader(8192);

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
        catch (Exception e) {
            System.out.println("Unable to read file");
            e.printStackTrace();
            return null;
        }
    }

    public Tile[][] getMap() {
        return map;
    }

    public Position getEntryTilePosition() {
        return entryTilePosition;
    }

    /**
     * Seeks for a specific tile from position
     * @param x the x position
     * @param y the y position
     * @return the tile in position (x, y)
     */
    public Tile getTileFromPosition(int x, int y){
        x = x / 64;
        y = y / 64;

        int line = y;
        int column = x;

        return map[line][column];
    }

    /**
     * Selects a lite
     * @param tile the tile selected
     */
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

    /**
     * Seeks for the map's entry tile
     * @return the tile's position
     */
    private Position findEntryTilePosition(){
        for (int i = 0; i < nbTiles; i++) {
                if (map[i][0].mapElement.name().contains("CHEMIN_GAUCHE") ||
                        map[i][0].mapElement.name().contains("CHEMIN_HORIZONTAL")) {
                    return new Position(0, (nbTiles-1 - i) * 64);
                }
            }
        return null;
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
