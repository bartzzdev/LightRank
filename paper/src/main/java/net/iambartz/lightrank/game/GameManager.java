package net.iambartz.lightrank.game;

import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.LightRankApi;
import net.iambartz.lightrank.api.game.*;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.game.def.duel.DuelGame;
import net.iambartz.lightrank.game.def.duel.DuelGameInformation;
import net.iambartz.lightrank.game.def.duel.DuelGameQueue;
import net.iambartz.lightrank.game.def.duel.DuelPlayer;
import net.iambartz.lightrank.game.def.ranking.RankingGame;
import net.iambartz.lightrank.game.def.ranking.RankingGameQueue;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    private final Map<String, Game> gameSessions;
    private final Map<UUID, GameInformation> gameInformation;
    private final Map<String, GameQueue> gameQueues;
    private int sessionCounter = 0;

    public GameManager(LightRankApi api, RankPlugin plugin) {
        this.gameSessions = new ConcurrentHashMap<>();
        this.gameInformation = new ConcurrentHashMap<>();
        this.gameQueues = new ConcurrentHashMap<>();
        this.registerPaperGames(api, plugin);
    }

    private void registerPaperGames(LightRankApi api, RankPlugin plugin) {
        final GameRegister gameRegister = api.getGameRegister();
        gameRegister.registerGame(Games.DUEL);
        this.gameQueues.put(Games.DUEL, new DuelGameQueue(plugin));
        gameRegister.registerGame(Games.RANKING);
        this.gameQueues.put(Games.RANKING, new RankingGameQueue(plugin));
    }

    private String getRandomSessionId(String gameName) {
        return gameName + "-" + ++this.sessionCounter;
    }

    public boolean isPlaying(UUID uuid) {
        return this.gameInformation.containsKey(uuid);
    }

    public boolean isPlaying(Player player) {
        return this.isPlaying(player.getUniqueId());
    }

    public boolean isPlaying(PlayerSession session) {
        return this.isPlaying(session.getUniqueId());
    }

    public boolean isPlayingDuel(UUID uuid) {
        final GameInformation information = this.gameInformation.get(uuid);
        if (information == null) {
            return false;
        }

        return this.isPlayingGame(information, Games.DUEL);
    }

    public boolean isPlayingGame(GameInformation information, String name) {
        if (information.getGameName() == name) {
            return true;
        }
        return information.getGameName().equals(name);
    }

    public boolean isPlayingRanking(UUID uuid) {
        final GameInformation information = this.gameInformation.get(uuid);
        if (information == null) {
            return false;
        }
        return this.isPlayingGame(information, Games.RANKING);
    }

    public boolean isPlayingRanking(PlayerSession session) {
        return this.isPlayingRanking(session.getUniqueId());
    }

    public boolean isPlayingRanking(Player player) {
        return this.isPlayingRanking(player.getUniqueId());
    }

    public boolean isPlayingDuel(Player player) {
        return this.isPlayingDuel(player.getUniqueId());
    }

    public boolean isPlayingDuel(PlayerSession session) {
        return this.isPlayingDuel(session.getUniqueId());
    }

    public GameInformation getGameInformation(PlayerSession session) {
        return this.gameInformation.get(session.getUniqueId());
    }

    public DuelGame createDuelGame(RankPlugin plugin, PlayerSession requester, PlayerSession receiver) throws ReachPlayerLimitException {
        final DuelGame duelGame = new DuelGame(plugin, this.getRandomSessionId(Games.DUEL), requester, receiver);
        duelGame.getGameSettings().enableKits();
        this.prepareDuelSession(duelGame);
        this.createDuelGameInformation(duelGame.getSessionId(), requester, receiver);
        return duelGame;
    }

    public RankingGame createRankingGame(RankPlugin plugin, PlayerSession requester, PlayerSession receiver) {
        final RankingGame rankingGame = new RankingGame(plugin, this.getRandomSessionId(Games.RANKING), requester, receiver);
        rankingGame.getGameSettings().enableKits();
        this.prepareRankingSession(rankingGame);
        this.createRankingGameInformation(rankingGame.getSessionId(), requester, receiver);
        return rankingGame;
    }

    private void prepareRankingSession(RankingGame game) {
        this.gameSessions.put(game.getSessionId(), game);
    }

    public void removeDuelGame(String sessionId) {
        final DuelGame game = (DuelGame) this.gameSessions.remove(sessionId);
        if (game != null) {
            game.getPlayers().forEach(player -> {
                if (this.isPlaying(player)) {
                    this.gameInformation.remove(player.getUniqueId());
                }
            });
        }
    }

    public void removeRankingGame(String sessionId) {
        final RankingGame game = (RankingGame) this.gameSessions.remove(sessionId);
        if (game != null) {
            game.getPlayers().forEach(player -> {
                if (this.isPlaying(player)) {
                    this.gameInformation.remove(player.getUniqueId());
                }
            });
        }
    }

    public void removeRankingGame(Game game) {
        this.removeRankingGame(game.getSessionId());
    }

    public void removeDuelGame(DuelGame game) {
        this.removeDuelGame(game.getSessionId());
    }

    public DuelGame createDuelGame(RankPlugin plugin, DuelPlayer requester, DuelPlayer receiver) throws ReachPlayerLimitException {
        final DuelGame duelGame = new DuelGame(plugin, this.getRandomSessionId(Games.DUEL), requester, receiver);
        duelGame.getGameSettings().enableKits();
        this.prepareDuelSession(duelGame);
        this.createDuelGameInformation(duelGame.getSessionId(), requester, receiver);
        return duelGame;
    }

    private void prepareDuelSession(DuelGame game) {
        this.gameSessions.put(game.getSessionId(), game);
    }

    public GameQueue getDuelGameQueue() {
        return this.gameQueues.get(Games.DUEL);
    }

    public GameQueue getRankingGameQueue() {
        return this.gameQueues.get(Games.RANKING);
    }

    public Game getGameSession(String sessionId) {
        return this.gameSessions.get(sessionId);
    }

    private void createDuelGameInformation(String sessionId, PlayerSession... sessions) {
        for (PlayerSession session : sessions) {
            this.gameInformation.put(session.getUniqueId(), new DuelGameInformation(sessionId));
            session.setCurrentSessionId(sessionId);
        }
    }

    private void createRankingGameInformation(String sessionId, PlayerSession... sessions) {
        for (PlayerSession session : sessions) {
            this.gameInformation.put(session.getUniqueId(), new GameInformation() {
                @Override
                public String getGameName() {
                    return Games.RANKING;
                }

                @Override
                public String getSessionId() {
                    return sessionId;
                }
            });
        }
    }
}
