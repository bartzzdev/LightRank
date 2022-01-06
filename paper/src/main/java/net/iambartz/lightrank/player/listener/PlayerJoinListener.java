package net.iambartz.lightrank.player.listener;

import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.LightRankApi;
import net.iambartz.lightrank.api.RankApiProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerJoinListener implements Listener {
    private final RankPlugin    rankPlugin;
    private final LightRankApi  api;

    public PlayerJoinListener(RankPlugin plugin) {
        this.rankPlugin = plugin;
        this.api = RankApiProvider.get();
    }

    @EventHandler
    public void onSessionStart(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        this.api.getSessionManager().createPlayerSession(player);
    }

    @EventHandler
    public void onSessionFinish(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        this.api.getSessionManager().destroyPlayerSession(player);
    }
}
