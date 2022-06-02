package com.td.app.game.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.td.app.Helper;
import com.td.app.game.Position;

import java.io.BufferedReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Wave {

    public static int DELAY_BETWEEN_WAVES = 600;
    // Wave status
    protected boolean isOver;

    // Enemy & timer until next one
    protected LinkedHashMap<StandardEnemy, Integer> enemies;


    private Wave(int level) {
        enemies = new LinkedHashMap<>();
        isOver = false;
    }

    private Wave(BufferedReader reader, Position position) {
        enemies = new LinkedHashMap<>();
        isOver = false;

        try {
            String line;
            while ((line = reader.readLine()) != null && !line.equals("endwave")) {
                String[] splits = line.split(" ");
                enemies.put(new StandardEnemy(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]),
                        new Position(position.getX(), position.getY() + 32),
                        new Texture(Gdx.files.internal(splits[2]))), 60
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

    public static Wave createWaveFromLevel(int level){
        return new Wave(level);
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
