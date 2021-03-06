package com.td.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.HashMap;

public final class SoundHandler {
    private static final HashMap<String, Sound> soundMap = new HashMap<>();
    private static final ArrayList<Sound> loopedSound = new ArrayList<>();
    private static boolean isPaused;

    /**
     * Adds a sound file to the sound map
     * @param name sound's name used as key in the sound map
     * @param fileLocation path to file's location
     */
    public static void add(String name, String fileLocation) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(fileLocation));
        soundMap.put(name, sound);
    }

    /**
     * Plays a sound
     * @param name sound's name in sound map
     */
    public static void play(String name) {
        if (!isPaused && TowerDefense.pref.getBoolean("sound")) {
            if (soundMap.containsKey(name)) {
                soundMap.get(name).play(0.4F);
            }
        }
    }

    /**
     * Plays a sound in loop mode
     * @param name sound's name in sound map
     */
    public static void playLooping(String name) {
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

    /**
     * Stops a sound
     * @param name sound's name in sound map
     */
    public static void stop(String name) {
        if(soundMap.containsKey(name)){
            soundMap.get(name).stop();
            loopedSound.remove(soundMap.get(name));
        }
    }

    /**
     * Pauses all sounds
     */
    public static void pauseAll() {
        for (Sound sound: loopedSound) {
            sound.pause();
        }
        isPaused = true;
    }

    /**
     * Plays all sounds
     */
    public static void playAll() {
        if (TowerDefense.pref.getBoolean("sound")) {
            for (Sound sound : loopedSound) {
                sound.play();
            }
            isPaused = false;
        }
    }

    /**
     * Stops all sounds
     */
    public static void stopAll() {
        for (Sound sound : loopedSound) {
            sound.stop();
        }
        loopedSound.clear();
        isPaused = false;
    }

    /**
     * Clears sounds and paused condition
     */
    public static void dispose() {
        soundMap.clear();
        loopedSound.clear();
        isPaused = false;
    }
}
