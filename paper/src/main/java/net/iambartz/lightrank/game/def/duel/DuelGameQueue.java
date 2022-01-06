package net.iambartz.lightrank.game.def.duel;

import net.iambartz.lightrank.LightColor;
import net.iambartz.lightrank.RankPlugin;
import net.iambartz.lightrank.api.game.GameQueue;
import net.iambartz.lightrank.api.game.ReachPlayerLimitException;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.game.Games;

import java.util.ArrayList;
import java.util.List;

public class DuelGameQueue implements GameQueue {
    private final RankPlugin plugin;
    private final List<PlayerSession> playerList;

    public DuelGameQueue(RankPlugin plugin) {
        this.plugin = plugin;
        this.playerList = new ArrayList<>(24);
    }

    @Override
    public String getGameName() {
        return Games.DUEL;
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
        session.message(LightColor.QUEUE_BASE + "Searching for a duel opponent... " +
                LightColor.QUEUE_KEY + "[" + (this.playerList.size() + 1) + "/24]");
        this.playerList.add(session);
        this.search();
    }

    @Override
    public void removePlayer(PlayerSession session) {
        session.message(LightColor.QUEUE_BASE + "Left from the duels queue.");
        this.playerList.remove(session);
        this.search();
    }

    @Override
    public boolean search() {
        final int playersSize = this.playerList.size();
        if (playersSize <= 1) {
            return false;
        }

        for (int i = 0; (i + 1) < this.playerList.size(); i+=2) {
            final PlayerSession firstPlayer = this.playerList.get(i);
            final PlayerSession secondPlayer = this.playerList.get(i + 1);
            try {
                final DuelGame duelGame = this.plugin.getGameManager().createDuelGame(this.plugin, firstPlayer, secondPlayer);
                duelGame.message(LightColor.QUEUE_BASE + "Successfully found " +
                        LightColor.QUEUE_KEY + "an opponent " + LightColor.QUEUE_BASE + "for a duel match!");
                duelGame.start(true);
                this.playerList.remove(firstPlayer);
                this.playerList.remove(secondPlayer);
            } catch (ReachPlayerLimitException e) {
                System.out.println("Reached the limit");
                continue;
            }
        }
        return true;
    }
}
