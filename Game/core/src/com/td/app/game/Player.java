package com.td.app.game;

public class Player {
    public static double PASSIVE_CREDIT_TIMER = 1.8; // 2s
    private int remainingLives;
    private int credit;
    private boolean gameOver;

    /**
     * Creates and initializes a player
     */
    public Player() {
        this.remainingLives = 10;
        this.credit = 50;
        this.gameOver = false;
    }

    /**
     * Adds credit of player's credits
     * @param amount the amount of credit to add
     */
    public void addCredit(int amount){
        this.credit += amount;
    }

    /**
     * Removes credit of player's credits
     * @param amount the amount of credit to remove
     * @return whether credits were removed
     */
    public boolean removeCredit(int amount){
        if(this.credit < amount) {
            return false;
        }
        this.credit -= amount;
        return true;
    }

    /**
     * Removes player's life
     */
    public void removeLife(){
        this.remainingLives--;

        if (this.remainingLives <= 0) {
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
