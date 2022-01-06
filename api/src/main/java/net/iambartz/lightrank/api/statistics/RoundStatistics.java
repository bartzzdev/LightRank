package net.iambartz.lightrank.api.statistics;

public interface RoundStatistics extends Statistics {
    int getRoundsWon();
    int getRoundsLost();
    int getWinsStreak();
    int getLosesStreak();
}
