package net.iambartz.lightrank.statistics;

import net.iambartz.lightrank.api.statistics.Statistics;

public class BasicStatistics implements Statistics {
    private int gamesPlayed;

    public BasicStatistics() {
        this.gamesPlayed = 0;
    }

    public BasicStatistics(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
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
