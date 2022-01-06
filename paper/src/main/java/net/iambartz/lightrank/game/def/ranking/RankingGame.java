package net.iambartz.lightrank.game.def.ranking;

import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.game.Game;
import net.iambartz.lightrank.api.game.GameResult;
import net.iambartz.lightrank.api.game.GameSettings;
import net.iambartz.lightrank.api.game.GameState;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.game.DefaultCountdown;
import net.iambartz.lightrank.game.GamePostProcessEvent;
import net.iambartz.lightrank.game.GameStartEvent;
import net.iambartz.lightrank.game.Games;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RankingGame implements Game<RankingPlayer> {
    private final RankPlugin plugin;
    private final String name;
    private final String sessionId;
    private final Set<RankingPlayer> players;
    private final GameSettings settings;
    private GameState state;

    public RankingGame(RankPlugin plugin, String id, RankingPlayer... players) {
        this.plugin = plugin;
        this.name = Games.RANKING;
        this.sessionId = id;
        this.players = new HashSet<>();
        for (RankingPlayer player : players) this.players.add(player);
        this.updateGameSessionId(players);
        this.settings = new GameSettings(id);
        this.state = GameState.WAITING;
    }

    public RankingGame(RankPlugin plugin, String id, PlayerSession... sessions) {
        this.plugin = plugin;
        this.name = Games.RANKING;
        this.sessionId = id;
        this.players = new HashSet<>();
        this.wrapSessionsToRankingInstances(sessions);
        this.updateGameSessionId(sessions);
        this.settings = new GameSettings(id);
        this.state = GameState.WAITING;
    }

    private void wrapSessionsToRankingInstances(PlayerSession... sessions) {
        for (PlayerSession element : sessions) {
            final RankingPlayer player = new RankingPlayer(element, this);
            this.plugin.getRankingManager().store(player);
            this.players.add(player);
        }
    }

    private void updateGameSessionId(PlayerSession... sessions) {
        Arrays.stream(sessions).forEach(session -> session.setCurrentSessionId(this.sessionId));
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
        if (this.settings.isKitsEnabled()) {
            this.getPlayers().forEach(RankingPlayer::kit);
        }
    }

    @Override
    public void finish(GameResult result) {
        this.updateStatistics();
        this.setGameState(GameState.POST_PROCESSING);
        this.getPlayers().forEach(player -> {
            if (this.settings.isKitsEnabled()) player.loadInventoryBefore();
        });

        Bukkit.getPluginManager().callEvent(new GamePostProcessEvent(result, this));
        this.plugin.getGameManager().removeRankingGame(this);
    }

    @Override
    public void terminate() {

    }

    @Override
    public int getPlayersLimit() {
        return 2;
    }

    @Override
    public Set<RankingPlayer> getPlayers() {
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
        return this.settings;
    }
}
