package net.iambartz.lightrank.game.def.duel;

import net.iambartz.lightrank.api.game.GameInformation;
import net.iambartz.lightrank.game.Games;

public class DuelGameInformation implements GameInformation {
    private final String sessionId;

    public DuelGameInformation(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getGameName() {
        return Games.DUEL;
    }

    @Override
    public String getSessionId() {
        return this.sessionId;
    }
}
