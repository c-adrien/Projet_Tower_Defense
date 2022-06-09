package com.td.app.game.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.td.app.game.Position;

import java.io.BufferedReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Random;

public class Wave {
    public static int DELAY_BETWEEN_WAVES = 9; // About 10s
    protected boolean isOver;

    protected LinkedHashMap<StandardEnemy, Double> enemies;
    protected double delayBetweenEnemies;

    private static final Random random = new Random();

    /**
     * Creates a wave based on level
     * @param level the amount by which enemy's default stats are buffed (through ln function)
     * @param numberOfEnemies the number of enemies the wave is made of
     * @param position the enemy's spawning position
     */
    private Wave(int level, int numberOfEnemies, Position position) {
        enemies = new LinkedHashMap<>();
        isOver = false;

        for (int i = 0; i < numberOfEnemies; i++) {
            enemies.put(new StandardEnemy((int) ((random.nextInt(30) + 50) * (Math.log(level) + 1)),
                    random.nextInt(15) + 15,
                    new Position(position.getX() - 20, position.getY() + 32),
                    new Texture(Gdx.files.internal("textures/enemy/test.png"))),
                    (180 - 3 * Math.log(level))
            );
        }
    }

    /**
     * Creates a wave from file
     * @param reader the BufferedReader used to read the file
     * @param position the enemy's spawning position
     */
    private Wave(BufferedReader reader, Position position) {
        enemies = new LinkedHashMap<>();
        isOver = false;

        try {
            String line;
            line = reader.readLine();
            delayBetweenEnemies = Double.parseDouble(line.split("=")[1]);

            while ((line = reader.readLine()) != null && !line.equals("endwave")) {
                String[] splits = line.split(" ");
                enemies.put(new StandardEnemy(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]),
                        new Position(position.getX() - 20, position.getY() + 32),
                        new Texture(Gdx.files.internal(splits[2]))), delayBetweenEnemies
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes wave's status to over
     */
    public void waveEnded(){
        this.isOver = true;
    }

    // Getters
    //=====================================================
    public boolean isOver() {
        return isOver;
    }
    public LinkedHashMap<StandardEnemy, Double> getEnemies() {
        return enemies;
    }

    //=====================================================

    /**
     * Creates a wave based on level
     * @param level the amount by which enemy's default stats are buffed (through ln function)
     * @param numberOfEnnemies the number of enemies the wave is made of
     * @param position the enemy's spawning position
     * @return the wave created
     */
    public static Wave createWaveFromDifficulty(int level, int numberOfEnnemies, Position position) {
        return new Wave(level, (int) (Math.log(numberOfEnnemies) * 10), position);
    }

    /**
     * Creates a wave from a file
     * @param fileHandle the file's location
     * @param position the enemy's spawning position
     * @return the list of waves created
     */
    public static LinkedList<Wave> createWaveFromFile(FileHandle fileHandle, Position position) {
        LinkedList<Wave> waves = new LinkedList<>();

        try {
            BufferedReader reader = fileHandle.reader(8192);
            while (reader.readLine() != null) {
                waves.add(new Wave(reader, position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return waves;
    }
}
