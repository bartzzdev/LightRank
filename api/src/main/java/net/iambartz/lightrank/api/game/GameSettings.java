package net.iambartz.lightrank.api.game;

public class GameSettings {
    private final String sessionId;
    private boolean kitsEnabled;

    public GameSettings(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isKitsEnabled() {
        return this.kitsEnabled;
    }

    public void enableKits() {
        this.kitsEnabled = true;
    }

    public void disableKits() {
        this.kitsEnabled = false;
    }
}
