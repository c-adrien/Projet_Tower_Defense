package com.td.app.game.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.td.app.game.Position;
import com.td.app.game.map.Map;
import com.td.app.game.map.MapElements;
import com.td.app.game.map.Tile;
import com.td.app.game.tower.Projectile;

import java.util.ArrayList;

public class StandardEnemy extends Actor {

    public static ArrayList<StandardEnemy> enemies = new ArrayList<>();

    protected int MAXIMUM_HP;
    protected int HP;
    protected int speed;
    protected Position position;
    protected boolean isAlive;
    protected Texture texture;
    protected Sprite sprite;
    protected Tile currentTile;

    protected float freezeRemainingTime;

    public StandardEnemy(int MAXIMUM_HP, int speed, Position position, Texture texture) {
        this.MAXIMUM_HP = MAXIMUM_HP;
        this.HP = MAXIMUM_HP;
        this.speed = speed;
        this.position = position;
        this.isAlive = true;

        this.texture = texture;
        this.currentTile = null;
        this.freezeRemainingTime = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        int x = this.position.getX();
        int y = this.position.getY();

        if (x > 12*64 || y > 12*64 || !isAlive){
            return;
        }

        setBounds(0, 0, texture.getWidth(), texture.getHeight());
        sprite = new Sprite(texture);

        batch.draw(sprite, position.getX(), position.getY(), position.getX(), position.getY(),
                64/2, 64/2, 1, 1,0);
        super.draw(batch, parentAlpha);
    }

    // TODO fix
    public void freeze(int time){
        this.freezeRemainingTime += time;
    }

    public void receiveProjectile(Projectile projectile){
        receiveDamage(projectile.getDamage());
    }

    private void receiveDamage(int damage){
        this.HP -= damage;
        if (this.HP <= 0){
            die();
        }
    }

    public void die(){
        this.isAlive = false;
    }

    public static boolean intervalContains(int low, int high, int n) {
        return n >= low && n <= high;
    }

    public void update(float delta, Map map){
        if(freezeRemainingTime > 0){
            freezeRemainingTime = Math.max(0, freezeRemainingTime-delta);
        }

        if(isAlive && freezeRemainingTime <= 0) {

            int x = this.position.getX();
            int y = this.position.getY();

            // Si sorti du cadre
            if (x >= 12*64 || y >= 12*64){
                return;
            }

            // Si entré dans le cadre
            if(!(x < 0 || y < 0)){

                this.currentTile = map.getTileFromPosition(x, 728 - y + 32);
                // currentTile.select(); // Debug

                // Tuile (%64) + intervale à choisir => changement de direction à mi-chemin de la tuile
                if(intervalContains(28, 40, x%64) || intervalContains(28, 40, y%64)){
                    if (currentTile != null) {
                        if(!currentTile.mapElement.equals(MapElements.CHEMIN_HORIZONTAL)
                                && !currentTile.mapElement.equals(MapElements.CHEMIN_VERTICAL)){

                            switch (currentTile.mapElement){
                                case CHEMIN_BAS_DROITE:
                                    this.position.setAngle(0);
                                    break;

                                case CHEMIN_HAUT_DROITE:
                                    this.position.setAngle(0);
                                    break;

                                case CHEMIN_GAUCHE_BAS:
                                    this.position.setAngle((3 * Math.PI)/2);
                                    break;

                                case CHEMIN_GAUCHE_HAUT:
                                    this.position.setAngle(Math.PI/2);
                                    break;
                            }

                        }
                    }
                }
            }


            // Debug
//            System.out.println("Angle : " + position.getAngle());
//            if(currentTile != null) System.out.println(currentTile.mapElement.name());
//            System.out.println("X " + x);
//            System.out.println("Y " + y);


            // Nouvelles positions
            float i = (float) (delta * Math.cos(position.getAngle()));
            float j = (float) (delta * Math.sin(position.getAngle()));

            updatePosition(i, j);
        }
    }

    public void updatePosition(float i, float j){
        this.position.updateX(i);
        this.position.updateY(j);
    }

    //==================================================

    public static void addEnemy(StandardEnemy enemy){
        enemies.add(enemy);
    }

    public static void removeEnemy(StandardEnemy enemy){
        enemies.remove(enemy);
    }

    public static void updateEnemies(float delta, Map map){
        delta *= 10;

        // faster debug
        delta *= 10;

        for (StandardEnemy enemy: enemies) {
            enemy.update(delta, map);
        }
    }

    public Position getPosition() {
        return position;
    }
}
