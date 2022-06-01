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
        waves = Wave.createWaveFromFile(Gdx.files.internal("waves/waveLevel1.txt"), map.getEntryTilePosition());
        actualWave = waves.pop();
        numberOfWaves = waves.size();
    }

    public void update(Stage stage, float delta){
        // TODO delta debug
        delta = delta * 20;

        updateTowers(stage);
        updateProjectiles(delta, stage);
        updateEnemies(delta, stage);
        updateWaves(stage);

        if (player.isGameOver()) {
            // TODO: end game
            System.out.println("GAME OVER");
        }
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

    public void updateTowers(Stage stage){
        for(AbstractTower tower : towerArrayList){
            StandardEnemy enemy = tower.findTarget(enemyArrayList);

            if(enemy != null){
                tower.setTimer(tower.getTimer() - 1);
                if (tower.getTimer() <= 0) {
                    if (tower instanceof SimpleTower) {
                        tower.setTimer(SimpleTower.timer);
                    }
                    Projectile projectile = tower.sendProjectile(enemy);
                    addProjectile(projectile);
                    stage.addActor(projectile);
                }
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

    // Projectiles
    //======================================================
    public void addProjectile(Projectile projectile) {
        projectileArrayList.add(projectile);
    }

    public void removeProjectile(Projectile projectile, Stage stage) {
        Helper.removeActorFromStage(projectile, stage);
        projectileArrayList.remove(projectile);
    }
    public void updateProjectiles(float delta, Stage stage) {
        for (Projectile projectile : projectileArrayList) {
            if (!projectile.update(delta)) {
                removeProjectile(projectile, stage);
                break;
            }
        }
    }

    // Enemies
    //======================================================

    public void updateEnemies(float delta, Stage stage) {
        for (StandardEnemy enemy : enemyArrayList) {
            if (!enemy.isAlive() || !enemy.update(delta, map, player)) {
                removeEnemy(enemy, stage);
                break;
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
        } else if (waves.size() != 0) {
            if (delayBeforeNextWave-- <= 0) {
                delayBeforeNextWave = 600;
                actualWave = waves.pop();
            }
        } else {
            // TODO : win the game and unlock next level if from campaign
        }
    }

    // Others
    //======================================================

    public Map getMap() {
        return map;
    }
}
