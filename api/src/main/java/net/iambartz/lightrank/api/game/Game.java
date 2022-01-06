package net.iambartz.lightrank.api.game;

import net.iambartz.lightrank.api.player.GamePlayer;

import java.util.Set;

public interface Game<T extends GamePlayer> {
    String getSessionId();
    String getName();
    void start(boolean countdownBefore);
    void finish(GameResult result);
    void terminate();
    int getPlayersLimit();
    Set<T> getPlayers();
    GameState getGameState();
    void setGameState(GameState gameState);
    GameSettings getGameSettings();

    default void message(String text) {
        for (T player : this.getPlayers()) {
            player.message(text);
        }
    }

    default void updateStatistics() {
        for (T player : this.getPlayers()) {
            player.getStatistics().setGamesPlayed(player.getStatistics().getGamesPlayed() + 1);     //increase game statistics
        }
    }
}
