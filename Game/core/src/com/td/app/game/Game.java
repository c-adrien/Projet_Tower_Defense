package com.td.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.td.app.Helper;
import com.td.app.SoundHandler;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.enemy.Wave;
import com.td.app.game.map.Map;
import com.td.app.game.map.Tile;
import com.td.app.game.tower.AbstractTower;
import com.td.app.game.tower.Projectile;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    public enum GameState { GAME_RUNNING, GAME_PAUSED, GAME_WON, GAME_LOST }
    public enum GameMode { CAMPAIGN, ARCADE }

    private GameState gameState = GameState.GAME_RUNNING;
    private GameMode gameMode;
    private final Player player;
    private final Map map;

    private LinkedList<Wave> waves = new LinkedList<>();
    private Wave actualWave;
    private int waveNumber = 1;
    private int difficulty;
    private int numberOfEnnemies = 5;

    private int delayBeforeNextWave = Wave.DELAY_BETWEEN_WAVES;
    private int passiveCreditTimer = Player.PASSIVE_CREDIT_TIMER;

    private final ArrayList<AbstractTower> towerArrayList = new ArrayList<>();
    private final ArrayList<Projectile> projectileArrayList = new ArrayList<>();
    private final ArrayList<StandardEnemy> enemyArrayList = new ArrayList<>();

    public Game(Map map) {
        this.map = map;
        this.player = new Player();
    }

    public void initWaves(int level){
        waves = Wave.createWaveFromFile(Gdx.files.internal(String.format("waves/waveLevel%s.txt", level)),
                new Position(map.getEntryTilePosition().getX(), map.getEntryTilePosition().getY())
        );
        SoundHandler.playLooping("walking");
        actualWave = waves.pop();
    }

    public void initInfiniteWaves(int difficulty) {
        this.difficulty = difficulty;
        createRandomWave();
    }

    private void createRandomWave() {
        difficulty *= 1.3;
        numberOfEnnemies *= 1.3;
        actualWave = Wave.createWaveFromDifficulty(difficulty, numberOfEnnemies,
                new Position(map.getEntryTilePosition().getX(), map.getEntryTilePosition().getY())
        );
        SoundHandler.playLooping("walking");
    }

    public void update(Stage stage, float delta, Label creditLabel, Label lifeLabel, Label waveNumberLabel) {
        updateTowers(stage);
        updateProjectiles(delta, stage);
        updateEnemies(delta, stage, lifeLabel);
        updateWaves(stage, waveNumberLabel);
        updateCredit(creditLabel);
    }

    // Player commands
    //======================================================

    public AbstractTower placeTower(AbstractTower selectedTower, Tile tile, int screenX, int screenY) {
        if (player.removeCredit(selectedTower.getPrice())) {
            AbstractTower tower = selectedTower.createTower(tile, screenX / 64 * 64, 704 - screenY / 64 * 64); // Center texture on tile
            addTower(tower);

            SoundHandler.play("coins");

            return tower;
        }

        return null;
    }

    // Towers
    //======================================================

    public void updateTowers(Stage stage){
        for(AbstractTower tower : towerArrayList){
            StandardEnemy enemy = tower.findTarget(enemyArrayList);

            if(enemy != null){
                tower.setTimer(tower.getTimer() - 1);
                if (tower.getTimer() <= 0) {
                    tower.setTimer(tower.getINITIAL_TIMER());
                    Projectile projectile = tower.sendProjectile(enemy);
                    addProjectile(projectile);
                    stage.addActor(projectile);
                }
            }
        }
    }

    public void upgradeTower(AbstractTower tower) {
        if (tower.canUpgrade(player)) {
            player.removeCredit(tower.getUpgradePrice());
            tower.upgrade();
        }
    }

    public void sellTower(AbstractTower tower, Stage stage) {
        player.addCredit(tower.getSellPrice());
        tower.getHostingTile().setOccupied(false);
        removeTower(tower, stage);
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
            if (!projectile.update(delta, enemyArrayList)) {
                removeProjectile(projectile, stage);
                break;
            }
        }
    }

    // Enemies
    //======================================================

    public void updateEnemies(float delta, Stage stage, Label lifeLabel) {
        for (StandardEnemy enemy : enemyArrayList) {
            if (!enemy.isAlive()) {
                player.addCredit(enemy.getCreditDeathValue());
                removeEnemy(enemy, stage);
                break;
            }
            if (!enemy.update(delta, map)) {
                removeEnemy(enemy, stage);
                player.removeLife();
                lifeLabel.setText(player.getRemainingLives());
                if (player.isGameOver()) {
                    loseGame();
                }
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

    public void updateWaves(Stage stage, Label waveNumberLabel) {
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
                SoundHandler.stop("walking");
                actualWave.waveEnded();
                waveNumber++;
                waveNumberLabel.setText("Wave: " + waveNumber);
            }
        } else if (waves.size() != 0) {
            if (--delayBeforeNextWave <= 0) {
                actualWave = waves.pop();
                delayBeforeNextWave = Wave.DELAY_BETWEEN_WAVES;
                SoundHandler.playLooping("walking");
            }
        } else if (gameMode.equals(GameMode.ARCADE)) {
            if (--delayBeforeNextWave <= 0) {
                createRandomWave();
                delayBeforeNextWave = Wave.DELAY_BETWEEN_WAVES;
            }
        } else {
            winGame();
        }
    }

    // Others
    //======================================================

    public void updateCredit(Label creditLabel) {
        if (--passiveCreditTimer <= 0) {
            passiveCreditTimer = Player.PASSIVE_CREDIT_TIMER;
            player.addCredit(1);
            creditLabel.setText(player.getCredit());
        }
    }
    public void winGame() {
        gameState = GameState.GAME_WON;
    }
    public void loseGame() {
        gameState = GameState.GAME_LOST;
        SoundHandler.stop("walking");
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
    public GameState getGameState() {
        return gameState;
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Map getMap() {
        return map;
    }
    public Player getPlayer() {
        return player;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public ArrayList<StandardEnemy> getEnemyArrayList() {
        return enemyArrayList;
    }
}
