package com.td.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.HashMap;

public class SoundHandler {
    private static final HashMap<String, Sound> soundMap = new HashMap<>();
    private static final ArrayList<Sound> loopedSound = new ArrayList<>();
    private static boolean isPaused;

    public static void add(String name, String fileLocation){
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(fileLocation));
        soundMap.put(name, sound);
    }

    public static void play(String name){
        if (!isPaused && TowerDefense.pref.getBoolean("sound")) {
            if (soundMap.containsKey(name)) {
                soundMap.get(name).play(0.4F);
            }
        }
    }

    public static void playLooping(String name){
        if(soundMap.containsKey(name)){
            long id = soundMap.get(name).play(0.4F);
            if(id == -1) return;
            soundMap.get(name).setLooping(id, true);
            if (isPaused || !TowerDefense.pref.getBoolean("sound")) {
                soundMap.get(name).pause();
            }
            loopedSound.add(soundMap.get(name));
        }
    }

    public static void stop(String name){
        if(soundMap.containsKey(name)){
            soundMap.get(name).stop();
            loopedSound.remove(soundMap.get(name));
        }
    }

    public static void pauseAll() {
        for (Sound sound: loopedSound) {
            sound.pause();
        }
        isPaused = true;
    }

    public static void playAll() {
        if (TowerDefense.pref.getBoolean("sound")) {
            for (Sound sound : loopedSound) {
                sound.play();
            }
            isPaused = false;
        }
    }

    public static void stopAll() {
        for (Sound sound : loopedSound) {
            sound.stop();
        }
        loopedSound.clear();
        isPaused = false;
    }
}
