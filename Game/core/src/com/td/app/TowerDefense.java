package com.td.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.td.app.game.screen.game.ArcadeGameScreen;
import com.td.app.game.screen.game.CampaignGameScreen;
import com.td.app.game.screen.menu.*;

public class TowerDefense extends Game {
	private ArcadeMenuScreen arcadeMenuScreen;
	private ArcadeGameScreen arcadeGameScreen;
	private CampaignMenuScreen campaignMenuScreen;
	private CampaignGameScreen campaignGameScreen;
	private StartMenuScreen startMenuScreen;
	private SettingsScreen settingsScreen;

	public static Music music;
	public static Preferences pref;

	/**
	 * <p>
	 *     Creates an {@link com.badlogic.gdx.ApplicationListener} that initializes and handle all game's screens
	 * </p>
	 * <p>
	 *     It also loads game's save and all sounds used when created
	 * </p>
	 */
	public TowerDefense() {
		super();
	}

	@Override
	public void create () {
		pref = Gdx.app.getPreferences("towerDefensePrefs");
		music = Gdx.audio.newMusic(Gdx.app.getFiles().internal("sound/backgroundMusic.mp3"));
		music.setVolume(0.7F);
		music.setLooping(true);

		SoundHandler.add("click", "sound/click_ogg.ogg");
		SoundHandler.add("coins", "sound/coins_ogg.ogg");
		SoundHandler.add("walking", "sound/walking_ogg.ogg");
		SoundHandler.add("hit_enemy", "sound/hit_enemy_ogg.ogg");
		SoundHandler.add("kill_enemy", "sound/kill_enemy_ogg.ogg");

		arcadeMenuScreen = new ArcadeMenuScreen(this);
		campaignMenuScreen = new CampaignMenuScreen(this);
		startMenuScreen = new StartMenuScreen(this);
		settingsScreen = new SettingsScreen(this);

		if (!pref.contains("user")) {
			setScreen(new NewGameScreen(this));
			pref.putInteger("unlockedLevels", 1); // Campaign unlocked levels
			pref.putBoolean("bombTower", false); // Bomb tower locked
			pref.putBoolean("freezeTower", false); // Freeze tower locked
			pref.putBoolean("moreDamageTower", false); // MoreDamage tower locked
			pref.putInteger("arcadeLevel1", 1); // Wave record on arcade level 1
			pref.putInteger("arcadeLevel2", 1); // Wave record on arcade level 2
			pref.putInteger("arcadeLevel3", 1); // Wave record on arcade level 3
			pref.putBoolean("music", true); // Display music
			pref.putBoolean("sound", true); // Display sound
			pref.flush();
		} else {
			toStartMenu();
		}
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

	public void toArcadeGameScreen(int level) {
		arcadeGameScreen = new ArcadeGameScreen(this, level);
		setScreen(arcadeGameScreen);
	}

	public void newGame() {
		SoundHandler.dispose();
		pref.clear();
		pref.flush();
		create();
	}
}
