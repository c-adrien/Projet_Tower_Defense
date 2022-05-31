package com.td.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.td.app.Helper;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.enemy.Wave;
import com.td.app.game.map.Map;
import com.td.app.game.map.Tile;
import com.td.app.game.tower.AbstractTower;
import com.td.app.game.tower.Projectile;
import com.td.app.game.tower.SimpleTower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Game {

    private final Player player;
    private final Map map;

    private int numberOfWaves;
    private LinkedList<Wave> waves;
    private Wave actualWave;

    private int delayBeforeNextWave = 600;

    private final ArrayList<AbstractTower> towerArrayList = new ArrayList<>();
    private final ArrayList<Projectile> projectileArrayList = new ArrayList<>();
    private final ArrayList<StandardEnemy> enemyArrayList = new ArrayList<>();


    public Game(Map map) {
        this(map, 0);
    }

    public Game(Map map, int level) {
        this.map = map;
        this.player = new Player();

        initWaves(level);
    }

    public void initWaves(int level){
        waves = new LinkedList<>(Arrays.asList(Wave.createWaveFromFile(Gdx.files.internal("waves/wave_1.txt"), map.getEntryTilePosition()),
                Wave.createWaveFromFile(Gdx.files.internal("waves/wave_1.txt"), map.getEntryTilePosition())));
        actualWave = waves.pop();
        numberOfWaves = waves.size();
    }

    public void update(Stage stage, float delta){
        // TODO delta debug
        delta = delta * 20;

        updateTowers();
        updateWaves(stage);
        updateEnemies(delta, stage);
    }

    // Player commands
    //======================================================

    public AbstractTower placeTower(Tile tile, int screenX, int screenY){

        int x = screenX / 64;
        int y = screenY / 64;

        // TODO tower type
        SimpleTower simpleTower = new SimpleTower(tile, x*64, 704 - y*64);
        addTower(simpleTower);
        return simpleTower;
    }

    // Towers
    //======================================================

    public void updateTowers(){
        for(AbstractTower tower : towerArrayList){
            StandardEnemy enemy = tower.findTarget(enemyArrayList);

            if(enemy != null){
                tower.sendProjectile(enemy);
            }
        }
    }

    public void addTower(AbstractTower tower){
        towerArrayList.add(tower);
    }

    public void removeTower(AbstractTower tower, Stage stage){
        Helper.removeActorFromStage(tower, stage);
        towerArrayList.remove(tower);
    }

    // Enemies
    //======================================================

    public void updateEnemies(float delta, Stage stage) {
        for (StandardEnemy enemy : enemyArrayList) {
            if (!enemy.isAlive()) {
                removeEnemy(enemy, stage);
                break;
            } else {
                if (!enemy.update(delta, map)) {
                    break;
                }
            }
        }
    }

    public void addEnemy(StandardEnemy enemy){
        enemyArrayList.add(enemy);
    }

    public void removeEnemy(StandardEnemy enemy, Stage stage){
        Helper.removeActorFromStage(enemy, stage);
        enemyArrayList.remove(enemy);
    }

    // Waves
    //======================================================

    public void updateWaves(Stage stage) {
        if (!actualWave.isOver()) {
            if (!actualWave.getEnemies().isEmpty()) {
                java.util.Map.Entry<StandardEnemy, Integer> enemy;
                enemy = actualWave.getEnemies().entrySet().iterator().next();
                enemy.setValue(enemy.getValue() - 1);
                if (enemy.getValue() <= 0) {
                    addEnemy(enemy.getKey());
                    stage.addActor(enemy.getKey());
                    actualWave.getEnemies().remove(enemy.getKey());
                }
            } else if (enemyArrayList.isEmpty()) {
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

    // Others
    //======================================================

    public Map getMap() {
        return map;
    }
}
