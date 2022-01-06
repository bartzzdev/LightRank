package net.iambartz.lightrank.player;

import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.api.player.SessionManager;
import net.iambartz.lightrank.api.statistics.Statistics;
import net.iambartz.lightrank.statistics.BasicStatistics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SessionManagerImpl implements SessionManager {
    private final Map<UUID, PlayerSession> sessions;

    public SessionManagerImpl() {
        this.sessions = new HashMap<>();
    }

    @Override
    public PlayerSession createPlayerSession(Player player) {
        return this.sessions.put(player.getUniqueId(), new BasicPlayerSession(player));
    }

    @Override
    public PlayerSession createPlayerSession(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }

        return this.sessions.put(uuid, new BasicPlayerSession(player));
    }

    @Override
    public void destroyPlayerSession(UUID uuid) {
        this.sessions.remove(uuid);
    }

    @Override
    public void destroyPlayerSession(Player player) {
        this.sessions.remove(player.getUniqueId());
    }

    @Override
    public PlayerSession getOrCreate(UUID uuid) {
        return this.sessions.getOrDefault(uuid, this.createPlayerSession(uuid));
    }

    @Override
    public PlayerSession getOrCreate(Player player) {
        return this.sessions.getOrDefault(player.getUniqueId(), this.createPlayerSession(player));
    }

    @Override
    public Optional<PlayerSession> get(UUID uuid) {
        return Optional.ofNullable(this.sessions.get(uuid));
    }

    @Override
    public Optional<PlayerSession> get(Player player) {
        return this.get(player.getUniqueId());
    }
}
