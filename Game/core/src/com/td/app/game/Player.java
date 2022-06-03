package com.td.app.game;
public class Player {
    public static int PASSIVE_CREDIT_TIMER = 60;
    private int remainingLives;
    private int credit;
    private boolean gameOver;

    public Player() {
        this.remainingLives = 10;
        this.credit = 200;
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

    public void setGameOver() {
        gameOver = true;
    }

    public int getCredit() {
        return credit;
    }

    public int getRemainingLives() {
        return remainingLives;
    }

}
