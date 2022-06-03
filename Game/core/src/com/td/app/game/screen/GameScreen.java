package com.td.app.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.map.Map;
import com.td.app.game.map.Tile;
import com.td.app.game.tower.AbstractTower;
import com.td.app.game.tower.SimpleTower;

public abstract class GameScreen implements Screen, InputProcessor {
    public TowerDefense game;
    protected Game gamePlay;
    private Stage stage;
    private SpriteBatch batch;
    private AbstractTower selectedTower;
    private Image creditTexture;
    private Label creditLabel;
    private Image lifeTexture;
    private Label lifeLabel;
    private boolean isPaused;

    // Debug
    ShapeRenderer shapeRenderer;
    int x = 0;
    int y = 0;
    StandardEnemy enemy;
    float i = 0;
    float j = 0;

    public GameScreen(TowerDefense game) {
        this.game = game;

        // Debug
        shapeRenderer = new ShapeRenderer();
    }

    public void initGamePlay(Map map, int level) {
        this.gamePlay = new Game(map, level);
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();

        creditTexture = new Image(new Texture(Gdx.files.internal("textures/player/coin.png")));
        creditTexture.setPosition(stage.getWidth() - 50, stage.getHeight() - 50);
        creditTexture.setScale(0.15F);

        lifeTexture = new Image(new Texture(Gdx.files.internal("textures/player/heart.png")));
        lifeTexture.setPosition(Map.TOTAL_SIZE + 30, Map.TOTAL_SIZE - 50);
        lifeTexture.setScale(0.15F);

        creditLabel = new Label(String.valueOf(gamePlay.getPlayer().getCredit()) ,new Skin(Gdx.files.internal("skin/uiskin.json")));
        creditLabel.setPosition(creditTexture.getX() - 40, creditTexture.getY() + 15);
        creditLabel.setFontScale(1.2F);

        lifeLabel = new Label(String.valueOf(gamePlay.getPlayer().getRemainingLives()), new Skin(Gdx.files.internal("skin/uiskin.json")));
        lifeLabel.setPosition(lifeTexture.getX() - 15, lifeTexture.getY() + 15);
        lifeLabel.setFontScale(1.2F);

        AbstractTower simpleTower = new SimpleTower(new Position(Map.TOTAL_SIZE + 30, Map.TOTAL_SIZE - 300));

        stage.addActor(gamePlay.getMap());
        stage.addActor(lifeTexture);
        stage.addActor(lifeLabel);
        stage.addActor(creditTexture);
        stage.addActor(creditLabel);
        stage.addActor(simpleTower);

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
        gamePlay.update(stage, delta, creditLabel, lifeLabel);

        if (selectedTower != null) {
            // TODO create class to load texture to show selected tower
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 0, 1, 1);
            shapeRenderer.rect(selectedTower.getX(), selectedTower.getY(), selectedTower.getWidth(), selectedTower.getHeight());
            shapeRenderer.end();
        }

    }

    private void pauseScreen() {
        if (!isPaused) {
            isPaused = true;
            final Image pauseGame = new Image(new Texture(Gdx.files.internal("textures/game/gamePause.png")));
            pauseGame.setScale(0.7F);
            pauseGame.setPosition(stage.getWidth() * 0.15F, stage.getHeight() * 0.15F);
            // TODO remove debug
            pauseGame.debug();

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
            Image endGame;
            if (gamePlay.getGameState().equals(Game.GameState.GAME_WON)) {
                endGame = new Image(new Texture(Gdx.files.internal("textures/game/gameWin.png")));
            } else {
                endGame = new Image(new Texture(Gdx.files.internal("textures/game/gameLost.png")));
            }
            endGame.setScale(0.7F);
            endGame.setPosition(stage.getWidth() * 0.15F, stage.getHeight() * 0.15F);
            // TODO remove debug
            endGame.debug();

            TextButton exitButton = new TextButton("Exit", new Skin(Gdx.files.internal("skin/uiskin.json")));
            exitButton.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.input.setInputProcessor(null);
                    game.toStartMenu();
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


        // Debug
        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Selected tile
        shapeRenderer.setColor(1, 0, 0, 1); // Red line
        shapeRenderer.line(x, y, x+64, y+64);
        shapeRenderer.end();

        // Enemy
//        if(enemy != null){
//            i = enemy.getPosition().getX();
//            j = enemy.getPosition().getY();
//        }
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(0, 0, 1, 1); // Blue line
//        shapeRenderer.line(i, j, i+32, j+32);
//        shapeRenderer.end();
        if(enemy != null){
            i = enemy.getPosition().getX();
            j = enemy.getPosition().getY();
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 1, 1); // Blue line
        shapeRenderer.line(i, j, i+32, j+32);
        shapeRenderer.end();*/

        /*
        // Grid
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int k = 0; k < 24; k++) {
            shapeRenderer.setColor(0, 0, 1, 1); // Blue line
            shapeRenderer.line(0, k*32, 768, k*32);
        }
        shapeRenderer.end();
         */

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
        System.out.println("screen X : " + screenX);
        System.out.println("screen Y : " + screenY);
        System.out.println("pointer : " + pointer);
        System.out.println("button : " + button + "\n");

        Vector2 hover = stage.screenToStageCoordinates(new Vector2(screenX,screenY));
        Actor actor = stage.hit(hover.x,hover.y,true);

        if(actor instanceof Map){

            if(selectedTower != null){
                selectedTower.setSelected(false);
            }

            int x = screenX / 64;
            int y = screenY / 64;

//
//            int line = y;
//            int column = x;

            Tile tile = gamePlay.getMap().getTileFromPosition(screenX, screenY);

            if(tile.isSelected() && !tile.isOccupied()){
                if (selectedTower != null) {
                    AbstractTower tower = gamePlay.placeTower(selectedTower, tile, screenX, screenY);
                    if (tower != null) {
                        stage.addActor(tower);
                    }
                }

                // Debug
                //enemy = new StandardEnemy(50, 1, new Position(gamePlay.getMap().getEntryTilePosition().getX(),
                //        gamePlay.getMap().getEntryTilePosition().getY()+32),
                //        new Texture(Gdx.files.internal("textures/enemy/test.png")));

                // enemy = new StandardEnemy(60, 10, new Position(entryTilePosition.getX(), entryTilePosition.getY()+32),
                //                        new Texture(Gdx.files.internal("textures/enemy/test.png")));

                //gamePlay.addEnemy(enemy);
                //stage.addActor(enemy);
            }

            gamePlay.getMap().toggleTile(tile);
            // System.out.println(Tile.SELECTED_TILE);

            // Debug
            this.x = x*64;
            this.y = 704 - y*64;
        }

        // Select tower
        else if (actor instanceof AbstractTower) {
            if(selectedTower != null && selectedTower != actor){
                selectedTower.setSelected(false);
            }
            selectedTower = (AbstractTower) actor;
            selectedTower.setSelected(!selectedTower.isSelected());
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
