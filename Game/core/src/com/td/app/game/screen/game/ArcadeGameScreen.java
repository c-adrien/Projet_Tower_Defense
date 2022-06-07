package com.td.app.game.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.td.app.TowerDefense;
import com.td.app.game.Game;
import com.td.app.game.map.Map;

public class ArcadeGameScreen extends GameScreen {
    private final int LEVEL;

    public ArcadeGameScreen(TowerDefense game, int level) {
        super(game);
        this.LEVEL = level;
        Map map = new Map();
        initGamePlay(map, level);
    }

    @Override
    public void initGamePlay(Map map, int level) {
        this.gamePlay = new Game(map);
        gamePlay.initInfiniteWaves(level);
        gamePlay.setGameMode(Game.GameMode.ARCADE);
    }

    @Override
    public void show() {
        super.show();
        Label difficultyLabel = new Label("Difficulty: " + LEVEL, new Skin(Gdx.files.internal("skin/uiskin.json")));
        difficultyLabel.setPosition(waveNumberLabel.getX() - 10, waveNumberLabel.getY() - 30);

        stage.addActor(difficultyLabel);
    }

    @Override
    public void endGameDisplay() {
        super.endGameDisplay();

        Label difficultyLabelEndGame = new Label("Difficulty: " + LEVEL, new Skin(Gdx.files.internal("skin/uiskin.json")));
        difficultyLabelEndGame.setPosition(stage.getWidth() * 0.37F, stage.getHeight() * 0.4F);

        Label waveNumberLabelEndGame = new Label("Wave: " + gamePlay.getWaveNumber(), new Skin(Gdx.files.internal("skin/uiskin.json")));
        waveNumberLabelEndGame.setPosition(difficultyLabelEndGame.getX() + 250, difficultyLabelEndGame.getY());

        Label previousWaveRecord = new Label("Wave record: " + gamePlay.getWaveNumber(), new Skin(Gdx.files.internal("skin/uiskin.json")));
        previousWaveRecord.setPosition(waveNumberLabelEndGame.getX() - 20, waveNumberLabelEndGame.getY() - 50);

        stage.addActor(difficultyLabelEndGame);
        stage.addActor(waveNumberLabelEndGame);
        stage.addActor(previousWaveRecord);

        int waveRecord = TowerDefense.pref.getInteger(String.format("arcadeLevel%s", LEVEL));

        if (gamePlay.getWaveNumber() > waveRecord) {
            TowerDefense.pref.putInteger(String.format("arcadeLevel%s", LEVEL), gamePlay.getWaveNumber());
        }
    }
}
