package com.td.app.game;

import com.td.app.game.enemy.Wave;
import com.td.app.game.map.Map;

public class Game {

    private final Map map;
    private final Player player;

    private int numberOfWaves;
    private Wave[] waves;


    public Game(Map map) {
        this(map, 0);
    }

    public Game(Map map, int level) {
        this.map = map;
        this.player = new Player();

        initWaves(level);
    }



    public void initWaves(int level){
        waves = new Wave[]{};
        numberOfWaves = waves.length;
    }

    public Map getMap() {
        return map;
    }

}
