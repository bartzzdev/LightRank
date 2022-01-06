package net.iambartz.lightrank.api.player;

import net.iambartz.lightrank.api.invite.Invite;
import net.iambartz.lightrank.api.statistics.Statistics;

public interface PlayerSession extends PlayerCredentials, Requestor {
    Statistics getStatistics();
    String getCurrentSessionId();
    void setCurrentSessionId(String sessionId);
    boolean isPlaying();
    void message(String text);
    void clickMessage(String text, String command);
}
