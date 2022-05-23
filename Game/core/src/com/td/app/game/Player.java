package com.td.app.game;

import com.td.app.game.tower.AbstractTower;

public class Player {

    private int remainingLives;
    private int credit;
    private boolean gameOver;

    public Player() {
        this.remainingLives = 10;
        this.credit = 100;
        this.gameOver = false;
    }

    public void addCredit(int amount){
        this.credit += amount;
    }

    public boolean removeCredit(int amount){
        if(this.credit < amount) return false;
        this.credit -= amount;
        return true;
    }

    public void removeLife(){
        this.remainingLives--;
        if(this.remainingLives <= 0){
            this.gameOver = true;
        }
    }

    // TODO
    public boolean PlaceTower(AbstractTower tower){
        return false;
    }
}
