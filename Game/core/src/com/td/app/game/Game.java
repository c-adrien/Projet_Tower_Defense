package com.td.app.game;

import com.td.app.game.map.Map;

public class Game {

    private final Map map;
    private final Player player;

    public Game(Map map) {
        this.map = map;
        this.player = new Player();
    }

    public Map getMap() {
        return map;
    }
}
