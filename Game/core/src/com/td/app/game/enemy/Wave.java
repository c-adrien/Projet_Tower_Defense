package com.td.app.game.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.td.app.game.Position;

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

    private Wave(FileHandle fileHandle, Position position) {
        enemies = new LinkedHashMap<>();
        isOver = false;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(fileHandle)));
            String line;

            while ((line = reader.readLine()) != null){
                String[] splits = line.split(" ");

                enemies.put(new StandardEnemy(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), new Position(position.getX(), position.getY() + 32),
                        new Texture(Gdx.files.internal(splits[2]))), 60);
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

    public static Wave createWaveFromFile(FileHandle fileHandle, Position position){
        return new Wave(fileHandle, position);
    }

}
