package net.iambartz.lightrank.api;

import net.iambartz.lightrank.api.game.GameRegister;
import net.iambartz.lightrank.api.player.SessionManager;

public interface LightRankApi {
    SessionManager getSessionManager();
    GameRegister getGameRegister();
}
