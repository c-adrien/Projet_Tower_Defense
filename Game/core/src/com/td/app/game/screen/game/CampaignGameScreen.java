package com.td.app.game.screen.game;

import com.badlogic.gdx.Gdx;
import com.td.app.TowerDefense;
import com.td.app.game.Game;
import com.td.app.game.map.Map;
import com.td.app.game.screen.menu.CampaignMenuScreen;

public class CampaignGameScreen extends GameScreen {
    private final int LEVEL;

    /**
     * Creates the game and the screen for the campaign game mode
     * @param game the game screen handler
     * @param level the level's number
     */
    public CampaignGameScreen(TowerDefense game, int level) {
        super(game);
        LEVEL = level;
        Map map = new Map(Gdx.files.internal(String.format("maps/map_%s.txt", level)));
        initGamePlay(map, level);
    }

    @Override
    public void initGamePlay(Map map, int level) {
        this.gamePlay = new Game(map);
        gamePlay.initWaves(level);
        gamePlay.setGameMode(Game.GameMode.CAMPAIGN);
    }

    @Override
    public void endGameDisplay() {
        super.endGameDisplay();
        int lastLevelUnlock = TowerDefense.pref.getInteger("unlockedLevels");

        if (lastLevelUnlock == LEVEL && LEVEL < CampaignMenuScreen.NUMBER_OF_LEVEL) {
            switch (LEVEL) {
                case 3:
                    TowerDefense.pref.putBoolean("moreDamageTower", true);
                    break;

                case 5:
                    TowerDefense.pref.putBoolean("bombTower", true);
                    break;

                case 8:
                    TowerDefense.pref.putBoolean("freezeTower", true);
                    break;
            }

            TowerDefense.pref.putInteger("unlockedLevels", LEVEL + 1);
            TowerDefense.pref.flush();
        }
    }
}
