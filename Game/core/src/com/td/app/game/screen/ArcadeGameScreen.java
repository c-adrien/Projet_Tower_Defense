package com.td.app.game.screen;

import com.td.app.TowerDefense;
import com.td.app.game.Game;
import com.td.app.game.map.Map;

public class ArcadeGameScreen extends GameScreen {

    public ArcadeGameScreen(TowerDefense game, int difficulty) {
        super(game);
        Map map = new Map();
        initGamePlay(map, difficulty);
    }

    @Override
    public void initGamePlay(Map map, int level) {
        this.gamePlay = new Game(map);
        gamePlay.initInfiniteWaves(level);
        gamePlay.setGameMode(Game.GameMode.ARCADE);
    }
}
