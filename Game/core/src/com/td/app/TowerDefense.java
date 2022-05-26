package com.td.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.td.app.game.screen.*;

public class TowerDefense extends Game {
	private ArcadeMenuScreen arcadeMenuScreen;
	private ArcadeGameScreen arcadeGameScreen;
	private CampaignMenuScreen campaignMenuScreen;
	private CampaignGameScreen campaignGameScreen;
	private StartMenuScreen startMenuScreen;
	private SettingsScreen settingsScreen;

	public static Music music;
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

		if (!pref.contains("user")) {
			setScreen(new NewUserScreen(this));
			pref.putInteger("unlockedLevels", 1);
			pref.putBoolean("music", true);
			pref.putBoolean("sound", true);
		} else {
			toStartMenu();
		}

//		toCampaignGameScreen(1);
//		toArcadeGameScreen();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		pref.flush();
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
	public void toCampaignGameScreen(int level) {
		campaignGameScreen = new CampaignGameScreen(this, level);
		setScreen(campaignGameScreen);
	}
	public void toArcadeGameScreen() {
		arcadeGameScreen = new ArcadeGameScreen(this);
		setScreen(arcadeGameScreen);
	}

	public void newGame() {
		pref.clear();
		pref.flush();
		create();
	}

	private void unlockCampaignLevels() {

	}
}
