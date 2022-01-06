package net.iambartz.lightrank;

import co.aikar.commands.PaperCommandManager;
import net.iambartz.lightrank.api.RankApiProvider;
import net.iambartz.lightrank.api.player.SessionManager;
import net.iambartz.lightrank.game.GameManager;
import net.iambartz.lightrank.game.def.duel.DuelCommand;
import net.iambartz.lightrank.game.def.duel.DuelListener;
import net.iambartz.lightrank.game.def.ranking.RankingCommand;
import net.iambartz.lightrank.game.def.ranking.RankingListener;
import net.iambartz.lightrank.game.def.ranking.RankingManager;
import net.iambartz.lightrank.player.SessionManagerImpl;
import net.iambartz.lightrank.player.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RankPlugin extends JavaPlugin {
    private SessionManager  sessionManager;
    private GameManager     gameManager;
    private RankingManager  rankingManager;

    @Override
    public void onLoad() {
        this.initializeComponents();
    }

    @Override
    public void onEnable() {
        this.initializeListeners();
        this.initializeCommands();
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    private void initializeComponents() {
        this.sessionManager = new SessionManagerImpl();
        RankApiProvider.register(this.sessionManager);
        this.gameManager = new GameManager(RankApiProvider.get(), this);
        this.rankingManager = new RankingManager();
    }

    private void initializeListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new DuelListener(this), this);
        pluginManager.registerEvents(new RankingListener(this), this);
    }

    private void initializeCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerDependency(SessionManager.class, this.sessionManager);
        commandManager.registerDependency(GameManager.class, this.gameManager);
        commandManager.registerCommand(new DuelCommand());
        commandManager.registerCommand(new RankingCommand());
    }

    public RankingManager getRankingManager() {
        return this.rankingManager;
    }
}
