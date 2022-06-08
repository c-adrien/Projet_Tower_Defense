package com.td.app;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Helper {

    /**
     * Deprecated
     */
    public static BufferedReader getBufferedReader(FileHandle fileHandle){
        List<String> patterns = Arrays.asList("Game/assets/", "./assets/", "");

        for (String pattern: patterns) {
            try {
                File file = new File(pattern+ fileHandle.path());
                return new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ignored) {}
        }
        return null;
    }

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
