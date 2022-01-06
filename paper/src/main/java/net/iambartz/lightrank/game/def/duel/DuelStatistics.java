package net.iambartz.lightrank.game.def.duel;

import net.iambartz.lightrank.api.statistics.RoundStatistics;
import net.iambartz.lightrank.api.statistics.Statistics;

public class DuelStatistics implements RoundStatistics {
    private int gamesPlayed;
    private int roundsWon;
    private int roundsLost;
    private int winsStreak;
    private int losesStreak;

    public DuelStatistics(Statistics statistics, int roundsWon, int roundsLost, int winsStreak, int losesStreak) {
        this.gamesPlayed = statistics.getGamesPlayed();
        this.roundsWon = roundsWon;
        this.roundsLost = roundsLost;
        this.winsStreak = winsStreak;
        this.losesStreak = losesStreak;
    }

    public DuelStatistics(Statistics statistics) {
        this(statistics, 0, 0, 0, 0);
    }

    @Override
    public int getRoundsWon() {
        return this.roundsWon;
    }

    @Override
    public int getRoundsLost() {
        return this.roundsLost;
    }

    @Override
    public int getWinsStreak() {
        return this.winsStreak;
    }

    @Override
    public int getLosesStreak() {
        return this.losesStreak;
    }

    @Override
    public int getGamesPlayed() {
        return this.gamesPlayed;
    }

    @Override
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
}
