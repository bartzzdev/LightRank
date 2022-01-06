package net.iambartz.lightrank.game.def.ranking;

import net.iambartz.lightrank.LightColor;
import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.game.GameQueue;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.game.Games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankingGameQueue implements GameQueue {
    private final RankPlugin plugin;
    private final List<PlayerSession> playerList;

    public RankingGameQueue(RankPlugin plugin) {
        this.plugin = plugin;
        this.playerList = new ArrayList<>();
    }
    @Override
    public String getGameName() {
        return Games.RANKING;
    }

    private boolean canBeAddedToQueue(PlayerSession session) {
        if (this.playerList.size() >= 24) {
            session.message(LightColor.ERR + "Cannot join the queue due to the max players reached.");
            return false;
        }
        return true;
    }

    @Override
    public void addPlayer(PlayerSession session) {
        if (!this.canBeAddedToQueue(session)) return;
        session.message(LightColor.QUEUE_BASE + "Searching for the best ranking opponent... " + LightColor.QUEUE_KEY + "[" + (this.playerList.size() + 1) + "/24]");
        this.playerList.add(session);
        this.search();
    }

    @Override
    public void removePlayer(PlayerSession session) {
        session.message(LightColor.QUEUE_BASE + "Left from the ranking queue.");
        this.playerList.remove(session);
        this.search();
    }

    @Override
    public boolean search() {
        final int playersSize = this.playerList.size();
        if (playersSize < 2) {
            return false;
        }

        if (playersSize == 2) {
            final PlayerSession requester = this.playerList.get(0);
            final PlayerSession receiver = this.playerList.get(1);
            final RankingGame game = this.plugin.getGameManager().createRankingGame(this.plugin, requester, receiver);
            this.updateGameSessionId(game.getSessionId(), requester, receiver);
            game.message(LightColor.QUEUE_BASE + "Successfully found " +
                    LightColor.QUEUE_KEY + "an opponent " + LightColor.QUEUE_BASE + "for a ranking match! #" + game.getSessionId());
            game.start(true);
            this.playerList.clear();
            return true;
        } else if (playersSize > 2) {
            final PlayerSession base = this.playerList.get(0);
            final RankingPlayer rankingBase = this.getOrCreate(base);
            final int baseRating = rankingBase.getStatistics().calculateRating();
            int bestChoice = 1;
            int lastDelta = 0;
            for (int i = 1; i < playersSize; i++) {
                final PlayerSession comparisonSession = this.playerList.get(i);
                final RankingPlayer comparison = this.getOrCreate(comparisonSession);
                final int delta = baseRating - comparison.getStatistics().calculateRating();
                if ((delta < 100 && delta >= 0) || (delta > -100 && delta <= 0)) {
                    // create match
                    final RankingGame game = this.plugin.getGameManager().createRankingGame(this.plugin, base, comparisonSession);
                    game.message(LightColor.QUEUE_BASE + "Successfully found " +
                            LightColor.QUEUE_KEY + "an opponent " + LightColor.QUEUE_BASE + "for a ranking match with similar rating!");
                    game.start(true);
                    this.playerList.remove(base);
                    this.playerList.remove(comparison);
                    this.reorderPlayerList();
                    return this.search();
                }

                if (delta > 100) {
                    if (lastDelta > delta) {        // this iteration is the best choice
                        bestChoice = i;
                        lastDelta = delta;
                        continue;
                    }
                } else if (delta < -100) {          // this iteration is the best choice
                    if (lastDelta > (delta * -1)) {
                        bestChoice = i;
                        lastDelta = delta * -1;
                        continue;
                    }
                }
            }

            final RankingPlayer bestRanking = this.getOrCreate(this.playerList.get(bestChoice));
            if (bestRanking == null) {
                return false;
            }

            final RankingGame game = this.plugin.getGameManager().createRankingGame(this.plugin, base, bestRanking);
            game.message(LightColor.QUEUE_BASE + "Successfully found " +
                    LightColor.QUEUE_KEY + "an opponent " + LightColor.QUEUE_BASE + "for a ranking match with similar rating!");
            game.start(true);
            this.playerList.remove(base);
            this.playerList.remove(bestRanking);
            this.reorderPlayerList();
            return this.search();
        }
        return false;
    }

    private void updateGameSessionId(String sessionId, PlayerSession... sessions) {
        Arrays.stream(sessions).forEach(player -> player.setCurrentSessionId(sessionId));
    }

    private void reorderPlayerList() {
        final List<PlayerSession> copy = new ArrayList<>();
        for (PlayerSession player : this.playerList) {
            copy.add(player);
        }

        this.playerList.clear();
        copy.forEach(this.playerList::add);
    }

    private RankingPlayer getOrCreate(PlayerSession session) {
        return this.plugin.getRankingManager().safeLookup(session.getUniqueId()).orElse(new RankingPlayer(session));
    }
}
