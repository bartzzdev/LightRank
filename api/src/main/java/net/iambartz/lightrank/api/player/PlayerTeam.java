package net.iambartz.lightrank.api.player;

import java.util.Set;

public interface PlayerTeam<T extends GamePlayer> {
    Set<T> getPlayers();
    void message(String text);
}
