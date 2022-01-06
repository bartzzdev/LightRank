package net.iambartz.lightrank.game.def.duel;

import com.google.mu.util.BiOptional;
import net.iambartz.lightrank.LightColor;
import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.LightRankApi;
import net.iambartz.lightrank.api.RankApiProvider;
import net.iambartz.lightrank.api.game.Game;
import net.iambartz.lightrank.api.game.GameResult;
import net.iambartz.lightrank.api.game.GameState;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.game.GameManager;
import net.iambartz.lightrank.game.GamePostProcessEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Optional;

public class DuelListener implements Listener {
    private final RankPlugin plugin;
    private final GameManager gameManager;
    private final LightRankApi api;

    public DuelListener(RankPlugin plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.api = RankApiProvider.get();
    }

//    private boolean isEntityPlayer(Entity entity) {
//        return entity instanceof Player;
//    }

    private Optional<Player> isEntityPlayer(Entity entity) {
        if (entity instanceof Player) {
            return Optional.of((Player) entity);
        } else {
            return Optional.empty();
        }
    }

    private BiOptional<Player, Player> areEntitiesPlayers(Entity a, Entity b) {
        if (a instanceof Player && b instanceof Player) {
            return BiOptional.of((Player) a, (Player) b);
        } else {
            return BiOptional.empty();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {         // block damage other than entity attack in duel match
        final Entity entity = event.getEntity();
        this.isEntityPlayer(entity).ifPresent(damaged -> {
            final PlayerSession playerSession = RankApiProvider.get().getSessionManager().get(damaged).orElse(null);
            if (playerSession == null) {
                return;
            }

            if (!this.gameManager.isPlayingDuel(playerSession)) {
                return;
            }

            final Game duelGame = gameManager.getGameSession(playerSession.getCurrentSessionId());      // block damage during countdown
            if (duelGame.getGameState() == GameState.STARTING) {
                event.setCancelled(true);
            }

            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {     // block other players damage duel match members in-game.
        final Entity victim = event.getEntity();
        final Entity attacker = event.getDamager();
        this.areEntitiesPlayers(victim, attacker).ifPresent((damaged, damager) -> {
            final PlayerSession playerSession = this.api.getSessionManager().get(damaged).orElse(null);
            if (playerSession == null) {
                return;
            }

            if (!this.gameManager.isPlayingDuel(playerSession)) {
                return;
            }

            final Game duelGame = gameManager.getGameSession(playerSession.getCurrentSessionId());      // block damage during countdown
            if (duelGame.getGameState() == GameState.STARTING) {
                event.setCancelled(true);
            }

            final PlayerSession damagerSession = this.api.getSessionManager().get(damager).orElse(null);
            if (!damagerSession.getCurrentSessionId().equals(playerSession.getCurrentSessionId())) {            // not in the same duel match - cancel the damage
                event.setCancelled(true);
            }
        });

    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        this.isEntityPlayer(entity).ifPresent(player -> {
            final Player killer = entity.getKiller();
            if (killer == null) {
                return;
            }

            final PlayerSession playerSession = RankApiProvider.get().getSessionManager().get(player).orElse(null);
            if (playerSession == null) {
                return;
            }

            if (!this.gameManager.isPlayingDuel(playerSession)) {
                return;
            }

            final DuelGame duelGame = (DuelGame) this.gameManager.getGameSession(playerSession.getCurrentSessionId());
            if (!duelGame.getGameState().isPlaying()) {
                event.setCancelled(true);
                return;
            }

            duelGame.finish(new GameResult() {
                @Override
                public String[] getWinners() {
                    return new String[]{killer.getName()};
                }

                @Override
                public Game getGame() {
                    return duelGame;
                }
            });
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
            player.playSound(player.getLocation(),Sound.BLOCK_ANVIL_USE, 1f, 1f);
        }
    }
}
