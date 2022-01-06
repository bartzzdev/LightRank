package net.iambartz.lightrank.api.player;

import java.util.UUID;

public interface PlayerCredentials {
    UUID getUniqueId();
    String getPlayerName();
}
