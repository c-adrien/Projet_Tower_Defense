package com.td.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundHandler {
    private static final HashMap<String, Sound> soundMap = new HashMap<>();

    public static void add(String name, String fileLocation){
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(fileLocation));
        soundMap.put(name, sound);
    }

    public static void play(String name){
        if(soundMap.containsKey(name)){
            soundMap.get(name).play();
        }
    }

}
