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

    private double delayBeforeNextWave = Wave.DELAY_BETWEEN_WAVES;
    private double passiveCreditTimer = Player.PASSIVE_CREDIT_TIMER;

    private final ArrayList<AbstractTower> towerArrayList = new ArrayList<>();
    private final ArrayList<Projectile> projectileArrayList = new ArrayList<>();
    private final ArrayList<StandardEnemy> enemyArrayList = new ArrayList<>();

    /**
     * Creates game components (map and player)
     * @param map the map used for the game
     */
    public Game(Map map) {
        this.map = map;
        this.player = new Player();
    }

    /**
     * Initializes the waves
     * @param level the level's number
     */
    public void initWaves(int level) {
        waves = Wave.createWaveFromFile(Gdx.files.internal(String.format("waves/waveLevel%s.txt", level)),
                new Position(map.getEntryTilePosition().getX(), map.getEntryTilePosition().getY())
        );
        SoundHandler.playLooping("walking");
        actualWave = waves.pop();
    }

    /**
     * Initialize the infinite waves
     * @param difficulty the initial difficulty of the waves
     */
    public void initInfiniteWaves(int difficulty) {
        this.difficulty = difficulty;
        createRandomWave();
    }

    /**
     * <p>
     *     Creates a random wave
     * </p>
     * <p>
     *     Each time this function is called, the wave's difficulty scales up.
     *     The scaling gets faster as the initial difficulty is high
     * </p>
     */
    private void createRandomWave() {
        difficulty *= 1.3;
        numberOfEnnemies *= 1.3;
        actualWave = Wave.createWaveFromDifficulty(difficulty, numberOfEnnemies,
                new Position(map.getEntryTilePosition().getX(), map.getEntryTilePosition().getY())
        );
        SoundHandler.playLooping("walking");
    }

    /**
     * Updates every actors of the game (towers, projectiles, enemies, waves and player's credit)
     * @param stage the stage where actors are displayed
     * @param delta the time in seconds since the last render
     * @param creditLabel the label showing player's credit
     * @param lifeLabel the label showing player's life
     * @param waveNumberLabel the label showing the actual wave's number
     */
    public void update(Stage stage, float delta, Label creditLabel, Label lifeLabel, Label waveNumberLabel) {
        updateTowers(stage);
        updateProjectiles(delta, stage);
        updateEnemies(delta, stage, lifeLabel);
        updateWaves(delta, stage, waveNumberLabel);
        updateCredit(delta, creditLabel);
    }

    // Player commands
    //======================================================

    /**
     * Places a tower on stage if the player has enough credits
     * @param selectedTower the tower selected in game's shop
     * @param tile the tile where the tower is set
     * @param screenX the x position to set the tower
     * @param screenY the y position to set the tower
     * @return the tower created
     */
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

    /**
     * Updates all the towers present on stage (i.e. find a target and send a projectile)
     * @param stage the stage where tower are displayed
     */
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

    /**
     * Upgrades a tower on stage
     * @param tower the tower to upgrade
     */
    public void upgradeTower(AbstractTower tower) {
        if (tower.canUpgrade(player)) {
            player.removeCredit(tower.getUpgradePrice());
            tower.upgrade();
        }
    }

    /**
     * Sells a tower and delete it from stage
     * @param tower the tower to sell
     * @param stage the stage where the tower is displayed
     */
    public void sellTower(AbstractTower tower, Stage stage) {
        player.addCredit(tower.getSellPrice());
        tower.getHostingTile().setOccupied(false);
        removeTower(tower, stage);
    }

    /**
     * Adds a tower to the tower list
     * @param tower the tower to add to the list
     */
    public void addTower(AbstractTower tower) {
        towerArrayList.add(tower);
    }

    /**
     * Removes a tower of the tower list and from stage
     * @param tower the tower to remove
     * @param stage the stage where the tower is displayed
     */
    public void removeTower(AbstractTower tower, Stage stage) {
        Helper.removeActorFromStage(tower, stage);
        towerArrayList.remove(tower);
    }

    // Projectiles
    //======================================================

    /**
     * Adds a projectile to the projectile list
     * @param projectile the projectile to add to the list
     */
    public void addProjectile(Projectile projectile) {
        projectileArrayList.add(projectile);
    }

    /**
     * Removes a projectile of the projectile list and from stage
     * @param projectile the projectile to remove
     * @param stage the stage where the projectile is displayed
     */
    public void removeProjectile(Projectile projectile, Stage stage) {
        Helper.removeActorFromStage(projectile, stage);
        projectileArrayList.remove(projectile);
    }

    /**
     * Updates all the projectiles present on stage
     * @param delta the time in seconds since the last render
     * @param stage the stage where the projectiles are displayed
     */
    public void updateProjectiles(float delta, Stage stage) {
        for (Projectile projectile : projectileArrayList) {
            if (projectile.update(delta, enemyArrayList)) {
                removeProjectile(projectile, stage);
                break;
            }
        }
    }

    // Enemies
    //======================================================

    /**
     * Updates all the enemies present on stage
     * @param delta the time in seconds since the last render
     * @param stage the stage where the enemies are displayed
     * @param lifeLabel the label showing player's life
     */
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

    /**
     * Adds an enemy to the enemy list
     * @param enemy the enemy to add to the list
     */
    public void addEnemy(StandardEnemy enemy){
        enemyArrayList.add(enemy);
    }

    /**
     * Removes an enemy of the enemy list and from stage
     * @param enemy the enemy to remove
     * @param stage the stage where the enemy is displayed
     */
    public void removeEnemy(StandardEnemy enemy, Stage stage){
        Helper.removeActorFromStage(enemy, stage);
        enemyArrayList.remove(enemy);
    }

    // Waves
    //======================================================

    /**
     * Updates the waves
     * @param delta the time in seconds since the last render
     * @param stage the stage where waves are started
     * @param waveNumberLabel the label showing the wave's number
     */
    public void updateWaves(float delta, Stage stage, Label waveNumberLabel) {
        if (!actualWave.isOver()) {
            if (!actualWave.getEnemies().isEmpty()) {
                java.util.Map.Entry<StandardEnemy, Double> enemy;
                enemy = actualWave.getEnemies().entrySet().iterator().next();
                enemy.setValue(enemy.getValue() - delta);
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
            delayBeforeNextWave -= delta;
            if (delayBeforeNextWave <= 0) {
                actualWave = waves.pop();
                delayBeforeNextWave = Wave.DELAY_BETWEEN_WAVES;
                SoundHandler.playLooping("walking");
            }
        } else if (gameMode.equals(GameMode.ARCADE)) {
            delayBeforeNextWave -= delta;
            if (delayBeforeNextWave <= 0) {
                createRandomWave();
                delayBeforeNextWave = Wave.DELAY_BETWEEN_WAVES;
                SoundHandler.playLooping("walking");
            }
        } else {
            winGame();
        }
    }

    // Others
    //======================================================

    /**
     * Updates passive player's credit
     * @param delta the time in seconds since the last render
     * @param creditLabel the label showing player's credits
     */
    public void updateCredit(float delta, Label creditLabel) {
        passiveCreditTimer -= delta;
        if (passiveCreditTimer <= 0) {
            passiveCreditTimer = Player.PASSIVE_CREDIT_TIMER;
            player.addCredit(1);
            creditLabel.setText(player.getCredit());
        }
    }

    /**
     * Changes the game state to game won
     */
    public void winGame() {
        gameState = GameState.GAME_WON;
    }

    /**
     * Changes the game state to game lost
     */
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

    public GameMode getGameMode() {
        return gameMode;
    }
}
