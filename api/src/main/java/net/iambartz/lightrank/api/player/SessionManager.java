package net.iambartz.lightrank.api.player;

import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface SessionManager {
    PlayerSession createPlayerSession(Player player);
    PlayerSession createPlayerSession(UUID uuid);
    void destroyPlayerSession(UUID uuid);
    void destroyPlayerSession(Player player);
    PlayerSession getOrCreate(UUID uuid);
    PlayerSession getOrCreate(Player player);
    Optional<PlayerSession> get(UUID uuid);
    Optional<PlayerSession> get(Player player);
}
