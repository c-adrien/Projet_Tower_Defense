package com.td.app.game.enemy;

import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;

public class Wave {

    // Wave status
    protected boolean isOver;

    // Enemy & timer until next one
    protected LinkedHashMap<StandardEnemy, Integer> enemies;


    private Wave(int level) {
        enemies = new LinkedHashMap<>();
        isOver = false;
    }

    private Wave(FileHandle fileHandle) {
        enemies = new LinkedHashMap<>();
        isOver = false;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(fileHandle)));
            String line;

            while ((line = reader.readLine()) != null){
                String[] splits = line.split(" ");

                // TODO enemies
                enemies.put(null, Integer.parseInt(splits[1]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waveEnded(){
        this.isOver = true;
    }


    //=====================================================

    public static Wave createWaveFromLevel(int level){
        return new Wave(level);
    }

    public static Wave createWaveFromFile(FileHandle fileHandle){
        return new Wave(fileHandle);
    }
}
