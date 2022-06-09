package com.td.app;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Iterator;

public final class Helper {

    /**
     * Removes an actor from stage
     * @param actor the actor to remove
     * @param stage the stage where the actor is displayed
     */
    public static void removeActorFromStage(Actor actor, Stage stage){
        Iterator<Actor> actorIterator = stage.getActors().iterator();
        while (actorIterator.hasNext()) {
            if (actorIterator.next().equals(actor)) {
                actorIterator.remove();
                break;
            }
        }
    }
}
