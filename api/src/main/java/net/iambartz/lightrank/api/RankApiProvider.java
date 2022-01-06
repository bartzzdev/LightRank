package net.iambartz.lightrank.api;

import net.iambartz.lightrank.api.game.GameRegister;
import net.iambartz.lightrank.api.player.SessionManager;

public class RankApiProvider implements LightRankApi {
    private static LightRankApi     api;
    private final SessionManager     sessionManager;
    private final GameRegister      gameRegister;

    private RankApiProvider(SessionManager sessionManager) {
        RankApiProvider.api = this;
        this.sessionManager = sessionManager;
        this.gameRegister = new GameRegister();
    }

    public static LightRankApi get() {
        if (RankApiProvider.api == null) {
            throw new RuntimeException("Cannot access the API, because it has not been registered.");
        }
        return RankApiProvider.api;
    }

    public static void register(SessionManager sessionManager) {
        if (RankApiProvider.api != null) {
            throw new RuntimeException("Detected too many tries of API registration.");
        }

        RankApiProvider.api = new RankApiProvider(sessionManager);
    }

    public static void unregister() {
        RankApiProvider.api = null;
    }

    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    @Override
    public GameRegister getGameRegister() {
        return this.gameRegister;
    }
}
