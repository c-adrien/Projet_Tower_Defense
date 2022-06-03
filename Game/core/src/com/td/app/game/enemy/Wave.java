package com.td.app.game.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.td.app.Helper;
import com.td.app.game.Position;

import java.io.BufferedReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Random;

public class Wave {

    public static int DELAY_BETWEEN_WAVES = 600;
    // Wave status
    protected boolean isOver;

    // Enemy & timer until next one
    protected LinkedHashMap<StandardEnemy, Integer> enemies;
    protected int delayBetweenEnnemies;

    private static final Random random = new Random();


    private Wave(int level, int numberOfEnnemies, Position position) {
        enemies = new LinkedHashMap<>();
        isOver = false;

        for (int i = 0; i < numberOfEnnemies; i++) {
            enemies.put(new StandardEnemy((int) ((random.nextInt(30) + 50) * Math.log(level)),
                    random.nextInt(15) + 15,
                    new Position(position.getX() - 20, position.getY() + 32),
                    new Texture(Gdx.files.internal("textures/enemy/test.png"))),
                    (int) (180 - 3 * Math.log(level))
            );
        }
    }

    private Wave(BufferedReader reader, Position position) {
        enemies = new LinkedHashMap<>();
        isOver = false;

        try {
            String line;
            line = reader.readLine();
            delayBetweenEnnemies = Integer.parseInt(line.split("=")[1]);

            while ((line = reader.readLine()) != null && !line.equals("endwave")) {
                String[] splits = line.split(" ");
                enemies.put(new StandardEnemy(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]),
                        new Position(position.getX() - 20, position.getY() + 32),
                        new Texture(Gdx.files.internal(splits[2]))), delayBetweenEnnemies
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waveEnded(){
        this.isOver = true;
    }

    public boolean isOver() {
        return isOver;
    }
    public LinkedHashMap<StandardEnemy, Integer> getEnemies() {
        return enemies;
    }

    //=====================================================

    public static Wave createWaveFromDifficulty(int level, int numberOfEnnemies, Position position){
        return new Wave(level, (int) (Math.log(numberOfEnnemies) * 10), position);
    }

    public static LinkedList<Wave> createWaveFromFile(FileHandle fileHandle, Position position) {
        LinkedList<Wave> waves = new LinkedList<>();

        try {
            BufferedReader reader = Helper.getBufferedReader(fileHandle);
            while (reader.readLine() != null) {
                waves.add(new Wave(reader, position));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return waves;
    }

}
