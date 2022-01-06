package net.iambartz.lightrank.api.game;

import net.iambartz.lightrank.api.player.GamePlayer;
import net.iambartz.lightrank.api.player.PlayerSession;

public interface GameQueue {
    String getGameName();
    void addPlayer(PlayerSession session);
    void removePlayer(PlayerSession session);
    boolean search();
}
