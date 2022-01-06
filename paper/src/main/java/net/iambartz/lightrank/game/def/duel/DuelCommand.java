package net.iambartz.lightrank.game.def.duel;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.iambartz.lightrank.LightColor;
import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.RankApiProvider;
import net.iambartz.lightrank.api.game.ReachPlayerLimitException;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.api.player.SessionManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("duel")
@CommandPermission("lightrank.duel")
public class DuelCommand extends BaseCommand {
    @Dependency private RankPlugin plugin;
    @Dependency private SessionManager sessionManager;

    private static final ChatColor ERROR = net.md_5.bungee.api.ChatColor.of("#930000");
    private static final ChatColor SUCCESS = net.md_5.bungee.api.ChatColor.of("#0cfe7a");
    private static final ChatColor KEY_WORD = net.md_5.bungee.api.ChatColor.of("#f5dfa2");

    @Default
    public void duelQueue(Player sender) {
        final PlayerSession playerSession = this.sessionManager.getOrCreate(sender);
        this.plugin.getGameManager().getDuelGameQueue().addPlayer(playerSession);
    }

    @Subcommand("request")
    @Syntax("<playerName>")
    public void requestDuel(Player sender, String targetName) throws ReachPlayerLimitException {
        if (targetName.equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(LightColor.ERR + "Cannot request yourself.");
            return;
        }

        final Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage(KEY_WORD + targetName + ERROR + " is not accessible. Please try again later.");
            return;
        }

        final PlayerSession playerSession = this.sessionManager.getOrCreate(sender);
        final PlayerSession targetSession = this.sessionManager.getOrCreate(target);

        targetSession.addInvite(new DuelInvite(playerSession, targetSession));
        this.plugin.getGameManager().createDuelGame(this.plugin, playerSession, targetSession).start(true);
    }

    @Subcommand("accept")
    @CommandPermission("lightrank.system.execution")
    @Syntax("<playerName>")
    public void accept(Player player, String playerName) {
        final PlayerSession session = RankApiProvider.get().getSessionManager().getOrCreate(player);
    }
}
