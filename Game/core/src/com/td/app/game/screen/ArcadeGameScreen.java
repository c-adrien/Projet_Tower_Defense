package com.td.app.game.screen;

import com.td.app.TowerDefense;
import com.td.app.game.map.Map;

public class ArcadeGameScreen extends GameScreen {

    public ArcadeGameScreen(TowerDefense game) {
        super(game);


        // TODO ############# RANDOM MAP
        Map map = new Map();

        // TODO select/create random map
//        Map map = new Map(Gdx.files.internal("./maps/map_1.txt"));
        initGamePlay(map);
    }
}
