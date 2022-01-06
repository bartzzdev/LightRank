package net.iambartz.lightrank.game.def.ranking;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RankingManager {
    private final Map<UUID, RankingPlayer> players = new HashMap<>();

    public Optional<RankingPlayer> safeLookup(UUID uuid) {
        return Optional.ofNullable(this.players.get(uuid));
    }

    public void store(RankingPlayer player) {
        this.players.put(player.getUniqueId(), player);
    }
}
