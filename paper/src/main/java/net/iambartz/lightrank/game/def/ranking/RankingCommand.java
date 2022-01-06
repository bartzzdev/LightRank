package net.iambartz.lightrank.game.def.ranking;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.iambartz.lightrank.LightColor;
import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.api.player.SessionManager;
import org.bukkit.entity.Player;

@CommandAlias("ranking")
@CommandPermission("lightrank.ranking")
public class RankingCommand extends BaseCommand {
    @Dependency private RankPlugin plugin;
    @Dependency private SessionManager sessionManager;

    @Default
    public void rankingQueue(Player sender) {
        final PlayerSession session = this.sessionManager.getOrCreate(sender);
        this.plugin.getGameManager().getRankingGameQueue().addPlayer(session);
    }

    @Subcommand("getrank")
    public void checkRanking(Player sender) {
        final PlayerSession session = this.sessionManager.getOrCreate(sender);
        this.plugin.getRankingManager().safeLookup(session.getUniqueId())
                .ifPresent(player -> this.checkRankingMessage(player));
    }

    private void checkRankingMessage(RankingPlayer player) {
        final RankingStatistics statistics = player.getStatistics();
        player.message(" ");
        player.message(LightColor.RANKING_BASE + "  Your rating: " + LightColor.RANKING_KEY + statistics.calculateRating());
        player.message(LightColor.RANKING_BASE + "  Kills: " + LightColor.RANKING_KEY + statistics.getKills()
                + LightColor.RANKING_BASE + ", streak: " + LightColor.RANKING_KEY + statistics.getKillsStreak());
        player.message(LightColor.RANKING_BASE + "  Deaths: " + LightColor.RANKING_KEY + statistics.getKills()
                + LightColor.RANKING_BASE + ", streak: " + LightColor.RANKING_KEY + statistics.getDeathsStreak());
        player.message(" ");
    }
}
