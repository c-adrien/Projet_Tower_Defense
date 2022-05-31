package com.td.app.game.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.game.Position;
import com.td.app.game.enemy.StandardEnemy;
import com.td.app.game.map.Tile;

import java.util.ArrayList;


public abstract class AbstractTower extends Actor {

    public static ArrayList<AbstractTower> towers = new ArrayList<>();

    private int level;
    private static int MAXIMUM_LEVEL = 5;
    private int projectileSpeed;
    private int projectileRange;

    private int timer;
    private int sellPrice;
    private int upgradePrice;
    private final Tile hostingTile;

    private Position position;

    private Texture texture;
    private Sprite sprite;

    public AbstractTower(int projectileSpeed, int projectileRange,  int timer, int sellPrice, int upgradePrice,
                         Tile hostingTile, String imgPath, int positionX, int positionY) {
        this.level = 1;
        this.projectileSpeed = projectileSpeed;
        this.projectileRange = projectileRange;
        this.timer = timer;
        this.sellPrice = sellPrice;
        this.upgradePrice = upgradePrice;
        this.hostingTile = hostingTile;
        hostingTile.setOccupied(true);

        this.texture = new Texture(Gdx.files.internal(imgPath));

        this.position = new Position(positionX, positionY);
        towers.add(this);

    }

    // TODO
    public StandardEnemy findTarget(){

        for(int i = 0; i< StandardEnemy.enemies.size(); i++){
            int ennemiesPosX = StandardEnemy.enemies.get(i).getPosition().getX();
            int ennemiesPosY = StandardEnemy.enemies.get(i).getPosition().getY();
            double distance = Math.sqrt( Math.pow(position.getX() - ennemiesPosX, 2) + Math.pow(position.getY()-ennemiesPosY,2) );
 //           System.out.println("LOOKING FOR TARGET");
            if(distance < projectileRange){
                System.out.println("ENEMY IN RANGE");
                sendProjectile();
                return StandardEnemy.enemies.get(i);

                }
        }

        return null;
    }

    public abstract void sendProjectile();

    public static void updateTowers(){
        for(AbstractTower tower : AbstractTower.towers){
            tower.findTarget();
        }
    }

    public boolean canUpgrade(){
        return level < MAXIMUM_LEVEL;
    }

    public void upgrade(){
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setBounds(0, 0, texture.getWidth(), texture.getHeight());
        sprite = new Sprite(texture);

        batch.draw(sprite, position.getX(), position.getY(), position.getX(), position.getY(),
                texture.getWidth(), texture.getHeight(), 1, 1,0);
        super.draw(batch, parentAlpha);
    }

    public void destroy(){
        texture.dispose();
    }

    //========================================================

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getProjectileSpeed() {
        return projectileSpeed;
    }

    public void setProjectileSpeed(int projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public int getProjectileRange() {
        return projectileRange;
    }

    public void setProjectileRange(int projectileRange) {
        this.projectileRange = projectileRange;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getUpgradePrice() {
        return upgradePrice;
    }

    public void setUpgradePrice(int upgradePrice) {
        this.upgradePrice = upgradePrice;
    }

    public Tile getHostingTile() {
        return hostingTile;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
