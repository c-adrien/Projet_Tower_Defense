package com.td.app.game;
public class Player {
    public static double PASSIVE_CREDIT_TIMER = 1.8;
    private int remainingLives;
    private int credit;
    private boolean gameOver;

    public Player() {
        this.remainingLives = 10;
        this.credit = 50;
        this.gameOver = false;
    }

    public void addCredit(int amount){
        this.credit += amount;
    }

    public boolean removeCredit(int amount){
        if(this.credit < amount) {
            return false;
        }
        this.credit -= amount;
        return true;
    }

    public void removeLife(){
        this.remainingLives--;
        if(this.remainingLives <= 0){
            this.gameOver = true;
        }
    }
    public boolean isGameOver() {
        return gameOver;
    }

    public int getCredit() {
        return credit;
    }

    public int getRemainingLives() {
        return remainingLives;
    }

}
