package net.iambartz.lightrank.game.def.duel;

import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.game.*;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.game.DefaultCountdown;
import net.iambartz.lightrank.game.GamePostProcessEvent;
import net.iambartz.lightrank.game.GameStartEvent;
import net.iambartz.lightrank.game.Games;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;

public class DuelGame implements Game<DuelPlayer> {
    private final RankPlugin plugin;
    private final String name = Games.DUEL;
    private final String sessionId;
    private final Set<DuelPlayer> players;
    private final GameSettings gameSettings;
    private GameState state;

    public DuelGame(RankPlugin plugin, String id, PlayerSession... players) throws ReachPlayerLimitException {
        this.plugin = plugin;
        this.sessionId = id;
        if (players.length > 2) {
            throw new ReachPlayerLimitException("The players limit for this type of match is 2.");
        }

        this.players = new HashSet<>(2);
        for (PlayerSession session : players) {
            final DuelPlayer duelPlayer = new DuelPlayer(session, this);
            this.players.add(duelPlayer);
        }

        this.gameSettings = new GameSettings(id);
        this.state = GameState.WAITING;
    }

    public DuelGame(RankPlugin plugin, String id, DuelPlayer... players) throws ReachPlayerLimitException {
        this.plugin = plugin;
        this.sessionId = id;
        if (players.length > 2) {
            throw new ReachPlayerLimitException("The players limit for this type of match is 2.");
        }

        this.players = new HashSet<>(2);
        for (DuelPlayer player : players) {
            this.players.add(player);
        }

        this.gameSettings = new GameSettings(id);
        this.state = GameState.WAITING;
    }

    @Override
    public String getSessionId() {
        return this.sessionId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void start(boolean countdownBefore) {
        if (countdownBefore) {
            this.prepare();
            new DefaultCountdown(this.plugin, this, 30);
            return;
        }

        this.setGameState(GameState.PLAYING);
        Bukkit.getPluginManager().callEvent(new GameStartEvent(this.players));
    }

    private void prepare() {
        if (this.gameSettings.isKitsEnabled()) {
            this.getPlayers().forEach(DuelPlayer::kit);
        }
    }

    @Override
    public void finish(GameResult result) {
        this.updateStatistics();
        this.setGameState(GameState.POST_PROCESSING);
        if (this.gameSettings.isKitsEnabled()) {
            this.getPlayers().forEach(duelPlayer -> {
                duelPlayer.loadInventoryBefore();
                duelPlayer.setCurrentSessionId(Games.NONE);
            });
        }
        Bukkit.getPluginManager().callEvent(new GamePostProcessEvent(result, this));
        this.plugin.getGameManager().removeDuelGame(this);
    }

    @Override
    public void terminate() {
        this.setGameState(GameState.STOPPED);
    }

    @Override
    public int getPlayersLimit() {
        return 2;
    }

    @Override
    public Set<DuelPlayer> getPlayers() {
        return this.players;
    }

    @Override
    public GameState getGameState() {
        return this.state;
    }

    @Override
    public void setGameState(GameState gameState) {
        this.state = gameState;
    }

    @Override
    public GameSettings getGameSettings() {
        return this.gameSettings;
    }
}
