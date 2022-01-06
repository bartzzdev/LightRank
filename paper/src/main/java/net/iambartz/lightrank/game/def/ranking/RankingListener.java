package net.iambartz.lightrank.game.def.ranking;

import net.iambartz.lightrank.LightColor;
import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.LightRankApi;
import net.iambartz.lightrank.api.RankApiProvider;
import net.iambartz.lightrank.api.game.Game;
import net.iambartz.lightrank.api.game.GameResult;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.game.GameManager;
import net.iambartz.lightrank.game.GamePostProcessEvent;
import net.iambartz.lightrank.game.def.duel.DuelGame;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class RankingListener implements Listener {
    private final RankPlugin plugin;
    private final GameManager gameManager;
    private final LightRankApi api;

    public RankingListener(RankPlugin plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.api = RankApiProvider.get();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {         // block damage other than entity attack in duel match
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        final Player damaged = (Player) event.getEntity();
        final PlayerSession playerSession = RankApiProvider.get().getSessionManager().get(damaged).orElse(null);
        if (playerSession == null) {
            return;
        }

        if (!this.gameManager.isPlayingRanking(playerSession)) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {     // block other players damage ranking match members in-game.
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        final Player damaged = (Player) event.getEntity();
        final Player damager = (Player) event.getDamager();
        final PlayerSession playerSession = this.api.getSessionManager().get(damaged).orElse(null);
        if (playerSession == null) {
            return;
        }

        if (!this.gameManager.isPlayingRanking(playerSession)) {
            return;
        }

        final PlayerSession damagerSession = this.api.getSessionManager().get(damager).orElse(null);
        if (!damagerSession.getCurrentSessionId().equals(playerSession.getCurrentSessionId())) {            // not in the same ranking match - cancel the damage
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        final Player killer = entity.getKiller();
        if (killer == null) {
            return;
        }

        final Player player = (Player) entity;
        final PlayerSession loserSession = RankApiProvider.get().getSessionManager().get(player).orElse(null);
        if (loserSession == null) {
            return;
        }

        final RankingPlayer loserRanking = this.plugin.getRankingManager().safeLookup(loserSession.getUniqueId()).orElse(null);
        if (loserRanking == null) {
            return;
        }

        final PlayerSession winnerSession = RankApiProvider.get().getSessionManager().get(killer).orElse(null);
        if (winnerSession == null) {
            return;
        }

        final RankingPlayer winnerRanking = this.plugin.getRankingManager().safeLookup(winnerSession.getUniqueId()).orElse(null);
        if (winnerRanking == null) {
            return;
        }

        if (!this.gameManager.isPlayingRanking(loserSession)) {
            return;
        }

        final RankingGame rankingGame = (RankingGame) this.gameManager.getGameSession(loserSession.getCurrentSessionId());
        if (!rankingGame.getGameState().isPlaying()) {
            event.setCancelled(true);
            return;
        }

        final GameResult result = new GameResult() {
            @Override
            public String[] getWinners() {
                return new String[]{killer.getName()};
            }

            @Override
            public Game getGame() {
                return rankingGame;
            }
        };

        rankingGame.finish(result);
        winnerRanking.getStatistics().addOpponentRating(loserRanking.getStatistics().calculateRating());
        winnerRanking.getStatistics().increaseKills();
        loserRanking.getStatistics().increaseDeaths();
        final int winnerRating = winnerRanking.getStatistics().calculateRating();
        final int loserRating = loserRanking.getStatistics().calculateRating();
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendMessage(LightColor.RANKING_KEY + winnerSession.getPlayerName() + LightColor.RANKING_BASE + " just won a ranking match with "
            + LightColor.RANKING_KEY + loserSession.getPlayerName() + ChatColor.GRAY + "[" + LightColor.INVITE_ACC + winnerRating +
                    ChatColor.GRAY + "/" + LightColor.ERR + loserRating + ChatColor.GRAY + "]");
        });
    }

    private final ChatColor main = ChatColor.of("#A47878");
    private final ChatColor keyWord = ChatColor.of("#5A4C4C");

    @EventHandler
    public void onDuelFinish(GamePostProcessEvent event) {      // notify server about duel match winner
        final GameResult result = event.getResult();
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(keyWord + result.getWinners()[0] + main + " just won a duel match!");
        });
    }

    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent event) {          // block using commands during a duel match
        final Player player = event.getPlayer();
        if (this.gameManager.isPlayingDuel(player)) {
            event.setCancelled(true);
            player.sendMessage(LightColor.ERR + "Commands are blocked in a duel match.");
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1f, 1f);
        }
    }
}
