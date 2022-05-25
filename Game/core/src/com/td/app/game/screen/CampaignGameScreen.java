package com.td.app.game.screen;

import com.badlogic.gdx.Gdx;
import com.td.app.TowerDefense;
import com.td.app.game.map.Map;

public class CampaignGameScreen extends GameScreen {

    public CampaignGameScreen(TowerDefense game, int level) {
        super(game);

        // TODO select map
        Map map = new Map(Gdx.files.internal(String.format("./maps/map_%s.txt", level)));

        initGamePlay(map);

    }
}
