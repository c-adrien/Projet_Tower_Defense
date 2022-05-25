package com.td.app.game;

import com.td.app.game.enemy.Wave;
import com.td.app.game.map.Map;
import com.td.app.game.tower.AbstractTower;

import java.util.ArrayList;

public class Game {

    private final Map map;
    private final Player player;

    private int numberOfWaves;
    private Wave[] waves;

    private final ArrayList<AbstractTower> towerArrayList = new ArrayList<>();


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

    public void addTower(AbstractTower tower){
        towerArrayList.add(tower);
    }

    public void removeTower(AbstractTower tower){
        towerArrayList.remove(tower);
    }

    public Map getMap() {
        return map;
    }

}
