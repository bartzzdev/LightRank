package net.iambartz.lightrank.api.game;

import net.iambartz.lightrank.api.LightRankApi;

import java.util.HashSet;
import java.util.Set;

public class GameRegister {
    private final Set<String> registeredGames = new HashSet<>();

    public void registerGame(Game game) {
        this.registeredGames.add(game.getName());
    }

    public void registerGame(String gameName) {
        this.registeredGames.add(gameName);
    }

    public void unregisterGame(Game game) {
        this.registeredGames.remove(game.getName());
    }

    public void unregisterGame(String gameName) {
        this.registeredGames.remove(gameName);
    }

    public boolean isGameRegistered(String gameName) {
        return this.registeredGames.contains(gameName);
    }

    public boolean isGameRegistered(Game game) {
        return this.registeredGames.contains(game.getName());
    }
}
