package com.td.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.td.app.game.screen.*;

import java.util.Objects;

public class TowerDefense extends Game {
	private ArcadeMenuScreen arcadeMenuScreen;
	private CampaignMenuScreen campaignMenuScreen;
	private CampaignGameScreen campaignGameScreen;
	private StartMenuScreen startMenuScreen;
	private SettingsScreen settingsScreen;
	public Music music;

	public static Preferences pref;

	public TowerDefense() {
		super();
	}
	@Override
	public void create () {
		pref = Gdx.app.getPreferences("towerDefensePrefs");
		music = Gdx.audio.newMusic(Gdx.app.getFiles().internal("sound/backgroundMusic.mp3"));
		music.setVolume(0.7F);
		music.setLooping(true);
		// TODO Load sound files

		arcadeMenuScreen = new ArcadeMenuScreen(this);
		campaignMenuScreen = new CampaignMenuScreen(this);
		startMenuScreen = new StartMenuScreen(this);
		settingsScreen = new SettingsScreen(this);
		campaignGameScreen = new CampaignGameScreen(this);

		if (!pref.contains("user")) {
			setScreen(new NewUserScreen(this));
			pref.putBoolean("music", true);
			pref.putBoolean("sound", true);
		} else {
			toStartMenu();
		}
//		toCampaignGameScreen();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		// TODO Save prefs
		pref.putBoolean("music", music.isPlaying());
		pref.flush();

		arcadeMenuScreen.dispose();
		campaignMenuScreen.dispose();
		startMenuScreen.dispose();
		settingsScreen.dispose();
		music.dispose();
	}


	public void toArcadeMenuScreen() {
		setScreen(arcadeMenuScreen);
	}
	public void toCampaignMenuScreen() {
		setScreen(campaignMenuScreen);
	}
	public void toStartMenu() {
		setScreen(startMenuScreen);
	}
	public void toSettingsScreen() {
		setScreen(settingsScreen);
	}
	public void toCampaignGameScreen() {
		setScreen(campaignGameScreen);
	}

	public void newGame() {
		pref.clear();
		pref.flush();
		create();
	}
}
