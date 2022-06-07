package com.td.app.game.gui.level;

public class CampaignLevel extends Level {
    private boolean isLocked;

    public CampaignLevel(String imgPath, int level) {
        super(imgPath, level);
        isLocked = true;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void unlockLevel() {
        isLocked = false;
    }
}
