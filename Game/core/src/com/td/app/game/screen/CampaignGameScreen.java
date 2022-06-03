package com.td.app.game.screen;

import com.badlogic.gdx.Gdx;
import com.td.app.TowerDefense;
import com.td.app.game.map.Map;

public class CampaignGameScreen extends GameScreen {
    private final int LEVEL;
    public CampaignGameScreen(TowerDefense game, int level) {
        super(game);
        LEVEL = level;
        Map map = new Map(Gdx.files.internal(String.format("./maps/map_%s.txt", level)));
        initGamePlay(map, level);
    }
    @Override
    public void endGameDisplay() {
        super.endGameDisplay();
        int lastLevelUnlock = TowerDefense.pref.getInteger("level");
        if (lastLevelUnlock == LEVEL) {
            TowerDefense.pref.putInteger("level", LEVEL + 1);
            TowerDefense.pref.flush();
        }
    }
}
