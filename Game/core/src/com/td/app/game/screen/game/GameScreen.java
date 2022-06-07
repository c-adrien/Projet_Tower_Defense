package com.td.app.game.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.td.app.Helper;
import com.td.app.SoundHandler;
import com.td.app.TowerDefense;
import com.td.app.game.Game;
import com.td.app.game.Position;
import com.td.app.game.gui.ScreenButtonTexture;
import com.td.app.game.gui.ShopButton;
import com.td.app.game.map.Map;
import com.td.app.game.map.Tile;
import com.td.app.game.tower.*;

public abstract class GameScreen implements Screen, InputProcessor {
    public TowerDefense game;
    protected Game gamePlay;
    protected Stage stage;
    private SpriteBatch batch;
    private AbstractTower selectedTower;
    private Image creditTexture;
    private Label creditLabel;
    private Image lifeTexture;
    private Label lifeLabel;
    protected Label waveNumberLabel;

    private AbstractTower moreDamageTower;
    private AbstractTower freezeTower;
    private AbstractTower bombTower;

    private Image store;
    private Image upgradeButton;
    private Image sellButton;
    private Image selectedTowerPointer;
    private Label selectedTowerLevel;

    private Image simpleTowerCreditTexture;
    private Image moreDamageTowerCreditTexture;
    private Image freezeTowerCreditTexture;
    private Image bombTowerCreditTexture;

    private Label simpleTowerPrice;
    private Label moreDamageTowerPrice;
    private Label freezeTowerPrice;
    private Label bombTowerPrice;

    private Label upgradePrice;
    private Image updradeCreditTexture;
    private Label sellPrice;
    private Image sellCreditTexture;

    private ScreenButtonTexture speedController;
    private int speed;

    private boolean isPaused;

    public GameScreen(TowerDefense game) {
        this.game = game;
        this.speed = 1;
    }

    public abstract void initGamePlay(Map map, int level);

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        store = new Image(new Texture(Gdx.files.internal("textures/store/store.png")));
        store.setPosition(Map.TOTAL_SIZE, 0);

        speedController = new ScreenButtonTexture("textures/button/speedButton.png",
                ScreenButtonTexture.ButtonType.SPEED_CONTROLLER);
        speedController.setPosition(stage.getWidth() / 200, stage.getHeight() / 200);
        speedController.setSize(40, 40);

        upgradeButton = new ShopButton("textures/store/upgradeButton.png", ShopButton.ShopButtonType.UPGRADE);
        upgradeButton.setPosition(895, 768-577-64);

        sellButton = new ShopButton("textures/store/sellButton.png", ShopButton.ShopButtonType.SELL);
        sellButton.setPosition(1087, 768-577-64);

        creditTexture = new Image(new Texture(Gdx.files.internal("textures/player/coin.png")));
        creditTexture.setPosition(stage.getWidth() - 128, stage.getHeight() - 128);
        creditTexture.setScale(0.15F);

        lifeTexture = new Image(new Texture(Gdx.files.internal("textures/player/heart.png")));
        lifeTexture.setPosition(Map.TOTAL_SIZE + 96, Map.TOTAL_SIZE - 128);
        lifeTexture.setScale(0.15F);

        creditLabel = new Label(String.valueOf(gamePlay.getPlayer().getCredit()), new Skin(Gdx.files.internal("skin/uiskin.json")));
        creditLabel.setPosition(creditTexture.getX() - 40, creditTexture.getY()+15);
        creditLabel.setFontScale(1.2F);

        lifeLabel = new Label(String.valueOf(gamePlay.getPlayer().getRemainingLives()), new Skin(Gdx.files.internal("skin/uiskin.json")));
        lifeLabel.setPosition(lifeTexture.getX() - 15, lifeTexture.getY() + 15);
        lifeLabel.setFontScale(1.2F);

        waveNumberLabel = new Label("Wave: " + gamePlay.getWaveNumber(), new Skin(Gdx.files.internal("skin/uiskin.json")));
        waveNumberLabel.setPosition(lifeTexture.getX() + 130, lifeTexture.getY() + 30);

        selectedTowerPointer = new Image(new Texture(Gdx.files.internal("textures/menu/pointer.png")));
        selectedTowerPointer.setPosition(-100, -100);
        selectedTowerPointer.setScale(0.4F);

        upgradePrice = new Label("0", new Skin(Gdx.files.internal("skin/uiskin.json")));
        upgradePrice.setPosition(upgradeButton.getX() + 10, upgradeButton.getY() - 42);

        updradeCreditTexture = new Image(new Texture(Gdx.files.internal("textures/player/coin.png")));
        updradeCreditTexture.setScale(0.08F);
        updradeCreditTexture.setPosition(upgradePrice.getX() + 20, upgradePrice.getY() - 3);

        sellPrice = new Label("0", new Skin(Gdx.files.internal("skin/uiskin.json")));
        sellPrice.setPosition(sellButton.getX() + 10, sellButton.getY() - 42);

        sellCreditTexture = new Image(new Texture(Gdx.files.internal("textures/player/coin.png")));
        sellCreditTexture.setScale(0.08F);
        sellCreditTexture.setPosition(sellPrice.getX() + 20, sellPrice.getY() - 3);

        selectedTowerLevel = new Label("", new Skin(Gdx.files.internal("skin/uiskin.json")));
        selectedTowerLevel.setPosition(upgradeButton.getX() + 100, upgradeButton.getY() + 35);

        stage.addActor(gamePlay.getMap());

        stage.addActor(speedController);
        stage.addActor(store);
        stage.addActor(upgradeButton);
        stage.addActor(sellButton);
        stage.addActor(creditTexture);
        stage.addActor(lifeTexture);
        stage.addActor(creditLabel);
        stage.addActor(lifeLabel);
        stage.addActor(waveNumberLabel);
        stage.addActor(upgradePrice);
        stage.addActor(updradeCreditTexture);
        stage.addActor(sellPrice);
        stage.addActor(sellCreditTexture);
        stage.addActor(selectedTowerLevel);

        AbstractTower simpleTower = new SimpleTower(new Position(863, 544 + 1));

        simpleTowerPrice = new Label(String.valueOf(SimpleTower.price), new Skin(Gdx.files.internal("skin/uiskin.json")));
        simpleTowerPrice.setPosition(863 + 3, 544 + 1 - 64);
        simpleTowerPrice.setFontScale(1.1F);

        simpleTowerCreditTexture = new Image(new Texture(Gdx.files.internal("textures/player/coin.png")));
        simpleTowerCreditTexture.setPosition(simpleTowerPrice.getX() + 32, simpleTowerPrice.getY() - 3);
        simpleTowerCreditTexture.setScale(0.08F);

        stage.addActor(simpleTower);
        stage.addActor(simpleTowerPrice);
        stage.addActor(simpleTowerCreditTexture);

        if (TowerDefense.pref.getBoolean("moreDamageTower")) {
            moreDamageTower = new MoreDamageTower(new Position(863 + 128, 544 + 1));
            moreDamageTowerPrice = new Label(String.valueOf(MoreDamageTower.price), new Skin(Gdx.files.internal("skin/uiskin.json")));
            moreDamageTowerPrice.setPosition(863 + 3 + 128, 544 + 1 - 64);
            moreDamageTowerPrice.setFontScale(1.1F);

            moreDamageTowerCreditTexture = new Image(new Texture(Gdx.files.internal("textures/player/coin.png")));
            moreDamageTowerCreditTexture.setPosition(moreDamageTowerPrice.getX() + 32, moreDamageTowerPrice.getY() - 3);
            moreDamageTowerCreditTexture.setScale(0.08F);

            stage.addActor(moreDamageTower);
            stage.addActor(moreDamageTowerPrice);
            stage.addActor(moreDamageTowerCreditTexture);
        }

        if (TowerDefense.pref.getBoolean("freezeTower")) {
            freezeTower = new FreezeTower(new Position(863 + 128 + 128, 544 + 1));
            freezeTowerPrice = new Label(String.valueOf(FreezeTower.price), new Skin(Gdx.files.internal("skin/uiskin.json")));
            freezeTowerPrice.setPosition(863 + 3 + 128 + 128, 544 + 1 - 64);
            freezeTowerPrice.setFontScale(1.1F);

            freezeTowerCreditTexture = new Image(new Texture(Gdx.files.internal("textures/player/coin.png")));
            freezeTowerCreditTexture.setPosition(freezeTowerPrice.getX() + 32, freezeTowerPrice.getY() - 3);
            freezeTowerCreditTexture.setScale(0.08F);

            stage.addActor(freezeTower);
            stage.addActor(freezeTowerPrice);
            stage.addActor(freezeTowerCreditTexture);
        }

        if (TowerDefense.pref.getBoolean("bombTower")) {
            bombTower = new BombTower(new Position(863, 544 + 1 - 192));
            bombTowerPrice = new Label(String.valueOf(BombTower.price), new Skin(Gdx.files.internal("skin/uiskin.json")));
            bombTowerPrice.setPosition(863 + 3 , 544 + 1 - 64 - 192);
            bombTowerPrice.setFontScale(1.1F);

            bombTowerCreditTexture = new Image(new Texture(Gdx.files.internal("textures/player/coin.png")));
            bombTowerCreditTexture.setPosition(bombTowerPrice.getX() + 32, bombTowerPrice.getY() - 3);
            bombTowerCreditTexture.setScale(0.08F);

            stage.addActor(bombTower);
            stage.addActor(bombTowerPrice);
            stage.addActor(bombTowerCreditTexture);
        }

        stage.addActor(selectedTowerPointer);

        Gdx.input.setInputProcessor(this);
    }

    private void cleanStage() {
        batch.begin();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

        batch.end();
    }

    private void updateStage(float delta) {
        delta *= speed;

        // Automatically update prices
        if(selectedTower != null) {
            if(selectedTower.getLevel() == AbstractTower.MAXIMUM_LEVEL){
                upgradePrice.setText("--");
            }
            else {
                upgradePrice.setText(selectedTower.getUpgradePrice());
            }

            sellPrice.setText(selectedTower.getSellPrice());
            selectedTowerLevel.setText("Level: "+selectedTower.getLevel());
        }

        gamePlay.update(stage, delta, creditLabel, lifeLabel, waveNumberLabel);
    }

    private void pauseScreen() {
        if (!isPaused) {
            SoundHandler.pauseAll();
            isPaused = true;
            final Image pauseGame = new Image(new Texture(Gdx.files.internal("textures/game/gamePause.png")));
            pauseGame.setScale(0.7F);
            pauseGame.setPosition(stage.getWidth() * 0.15F, stage.getHeight() * 0.15F);

            final TextButton playButton = new TextButton("Play", new Skin(Gdx.files.internal("skin/uiskin.json")));
            final TextButton quitButton = new TextButton("Quit", new Skin(Gdx.files.internal("skin/uiskin.json")));
            playButton.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.input.setInputProcessor(null);
                    Helper.removeActorFromStage(pauseGame, stage);
                    Helper.removeActorFromStage(playButton, stage);
                    Helper.removeActorFromStage(quitButton, stage);
                    resume();
                }
            });
            playButton.setSize(pauseGame.getWidth() * 0.25F, 50);
            playButton.setPosition(pauseGame.getX() * 1.5F, pauseGame.getY() + 50);
            quitButton.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    Helper.removeActorFromStage(pauseGame, stage);
                    Helper.removeActorFromStage(playButton, stage);
                    Helper.removeActorFromStage(quitButton, stage);
                    SoundHandler.stopAll();
                    Gdx.input.setInputProcessor(null);
                    game.toStartMenu();
                    dispose();
                }
            });
            quitButton.setSize(pauseGame.getWidth() * 0.25F, 50);
            quitButton.setPosition(pauseGame.getX() * 3.5F, pauseGame.getY() + 50);

            stage.addActor(pauseGame);
            stage.addActor(playButton);
            stage.addActor(quitButton);

            Gdx.input.setInputProcessor(stage);
        }
    }

    public void endGameDisplay() {
        if (!isPaused) {
            isPaused = true;
            SoundHandler.pauseAll();

            Image endGame;
            if (gamePlay.getGameState().equals(Game.GameState.GAME_WON)) {
                endGame = new Image(new Texture(Gdx.files.internal("textures/game/gameWin.png")));
            } else {
                endGame = new Image(new Texture(Gdx.files.internal("textures/game/gameLost.png")));
            }
            endGame.setScale(0.7F);
            endGame.setPosition(stage.getWidth() * 0.15F, stage.getHeight() * 0.15F);

            TextButton exitButton = new TextButton("Exit", new Skin(Gdx.files.internal("skin/uiskin.json")));
            exitButton.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    SoundHandler.stopAll();
                    Gdx.input.setInputProcessor(null);

                    if(gamePlay.getGameMode() == Game.GameMode.ARCADE){
                        game.toArcadeMenuScreen();
                    }
                    else if(gamePlay.getGameMode() == Game.GameMode.CAMPAIGN){
                        game.toCampaignMenuScreen();
                    }
                    else {
                        game.toStartMenu();
                    }

                    dispose();
                }
            });
            exitButton.setSize(endGame.getWidth() * 0.3F, 50);
            exitButton.setPosition(endGame.getX() * 2.3F, endGame.getY() + 50);

            stage.addActor(endGame);
            stage.addActor(exitButton);

            Gdx.input.setInputProcessor(stage);
        }
    }

    @Override
    public void render(float delta) {
        if (delta > 0.1F) delta = 0.1F;
        switch (gamePlay.getGameState()) {
            case GAME_RUNNING:
                updateStage(delta);
                break;

            case GAME_PAUSED:
                pauseScreen();
                break;

            case GAME_WON:
            case GAME_LOST:
                endGameDisplay();
                break;
        }
        cleanStage();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        gamePlay.setGameState(Game.GameState.GAME_PAUSED);
        SoundHandler.stop("walking");
    }

    @Override
    public void resume() {
        SoundHandler.playAll();
        Gdx.input.setInputProcessor(this);
        gamePlay.setGameState(Game.GameState.GAME_RUNNING);
        isPaused = false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            pause();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Debug
        System.out.println("screen X : " + screenX);
        System.out.println("screen Y : " + screenY);
        System.out.println("pointer : " + pointer);
        System.out.println("button : " + button + "\n");

        Vector2 hover = stage.screenToStageCoordinates(new Vector2(screenX,screenY));
        Actor actor = stage.hit(hover.x,hover.y,true);

        // Debug
        System.out.println(actor.getClass());

        if (actor instanceof Map) {
            if(selectedTower != null){
                selectedTower.setSelected(false);
            }

            Tile tile = gamePlay.getMap().getTileFromPosition(screenX, screenY);
            if(tile.isSelected() && !tile.isOccupied()){
                if (selectedTower != null) {
                    AbstractTower tower = gamePlay.placeTower(selectedTower, tile, screenX, screenY);
                    if (tower != null) {
                        stage.addActor(tower);
                    }
                }
            }

            gamePlay.getMap().toggleTile(tile);
        }

        // Select tower
        if (actor instanceof AbstractTower) {
            if (selectedTower != null && selectedTower != actor) {
                selectedTower.setSelected(false);
            }

            selectedTower = (AbstractTower) actor;
            selectedTower.setSelected(!selectedTower.isSelected());

            if (selectedTower.getPosition().getX() > Map.TOTAL_SIZE) { // Shop tower selected
                selectedTowerPointer.setPosition(selectedTower.getPosition().getX() - 50, selectedTower.getPosition().getY() + 10);
                upgradePrice.setText("0");
                sellPrice.setText("0");
                selectedTowerLevel.setText("");
            } else {
                if(selectedTower.getLevel() == AbstractTower.MAXIMUM_LEVEL){
                    upgradePrice.setText("--");
                }
                else {
                    upgradePrice.setText(selectedTower.getUpgradePrice());
                }

                sellPrice.setText(selectedTower.getSellPrice());
                selectedTowerLevel.setText("Level: "+selectedTower.getLevel());
            }
        }

        if (actor instanceof ShopButton) {
            ShopButton shopButton = (ShopButton) actor;

            if (shopButton.getType().equals(ShopButton.ShopButtonType.SELL) && selectedTower.isSelected()) {
                gamePlay.sellTower(selectedTower, stage);
            }

            if (shopButton.getType().equals(ShopButton.ShopButtonType.UPGRADE) && selectedTower.isSelected()) {
                gamePlay.upgradeTower(selectedTower);
            }
        }

        if (actor instanceof ScreenButtonTexture) {
            ScreenButtonTexture screenButton = (ScreenButtonTexture) actor;

            final int speedValue = 4;

            if (screenButton.getType().equals(ScreenButtonTexture.ButtonType.SPEED_CONTROLLER)) {

                if (speed == 1) {
                    speed = speedValue;
                    AbstractTower.GAME_SPEED = speedValue / 2;
                }

                else if (speed == speedValue) {
                    speed = 1;
                    AbstractTower.GAME_SPEED = 1;
                }
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
