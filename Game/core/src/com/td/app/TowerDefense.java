package com.td.app;

import com.badlogic.gdx.Game;
import com.td.app.game.screen.*;

public class TowerDefense extends Game {
	private ArcadeMenuScreen arcadeMenuScreen;
	private CampaignMenuScreen campaignMenuScreen;
	private CampaignGameScreen campaignGameScreen;
	private StartMenuScreen startMenuScreen;
	private SettingsScreen settingsScreen;

	public TowerDefense() {
		super();
	}
	@Override
	public void create () {
		// TODO Check if save available

		toStartMenu();
		// toCampaignGameScreen();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		arcadeMenuScreen.dispose();
		campaignMenuScreen.dispose();
		startMenuScreen.dispose();
		settingsScreen.dispose();
	}


	public void toArcadeMenuScreen() {
		arcadeMenuScreen = new ArcadeMenuScreen(this);
		setScreen(arcadeMenuScreen);
	}
	public void toCampaignMenuScreen() {
		campaignMenuScreen = new CampaignMenuScreen(this);
		setScreen(campaignMenuScreen);
	}
	public void toStartMenu() {
		startMenuScreen = new StartMenuScreen(this);
		setScreen(startMenuScreen);
	}
	public void toSettingsScreen() {
		settingsScreen = new SettingsScreen(this);
		setScreen(settingsScreen);
	}
	public void toCampaignGameScreen() {
		campaignGameScreen = new CampaignGameScreen(this);
		setScreen(campaignGameScreen);
	}
}
