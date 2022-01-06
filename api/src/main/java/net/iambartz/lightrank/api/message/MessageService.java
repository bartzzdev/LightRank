package net.iambartz.lightrank.api.message;

import net.iambartz.lightrank.api.player.GamePlayer;
import net.iambartz.lightrank.api.player.PlayerSession;

public interface MessageService<T extends PlayerSession> {
    void message(String text);
}
