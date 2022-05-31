package com.td.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.enemy.Wave;
import com.td.app.game.map.Map;
import com.td.app.game.tower.AbstractTower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Game {

    private final Map map;
    private final Player player;

    private int numberOfWaves;
    private LinkedList<Wave> waves;
    private Wave actualWave;

    private int delayBeforeNextWave = 600;

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
        waves = new LinkedList<>(Arrays.asList(Wave.createWaveFromFile(Gdx.files.internal("Game/assets/waves/wave_1.txt"), map.getEntryTilePosition()),
                Wave.createWaveFromFile(Gdx.files.internal("Game/assets/waves/wave_1.txt"), map.getEntryTilePosition())));
        actualWave = waves.pop();
        numberOfWaves = waves.size();
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
    public void updateWaves(Stage stage) {
        if (!actualWave.isOver()) {
            if (!actualWave.getEnemies().isEmpty()) {
                java.util.Map.Entry<StandardEnemy, Integer> ennemy;
                ennemy = actualWave.getEnemies().entrySet().iterator().next();
                ennemy.setValue(ennemy.getValue() - 1);
                if (ennemy.getValue() <= 0) {
                    StandardEnemy.addEnemy(ennemy.getKey());
                    stage.addActor(ennemy.getKey());
                    actualWave.getEnemies().remove(ennemy.getKey());
                }
            } else if (StandardEnemy.enemies.isEmpty()) {
                actualWave.waveEnded();
            }
        } else if (delayBeforeNextWave-- <= 0) {
            delayBeforeNextWave = 600;
            if (waves.size() != 0) {
                actualWave = waves.pop();
            } else {
                // TODO : win the game and unlock next level if from campaign
            }
        }
    }

}
