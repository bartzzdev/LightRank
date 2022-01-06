package net.iambartz.lightrank.api.statistics;

public interface KDStatistics extends Statistics {
    double getKDRatio();
    int getKillsStreak();
    int getDeathsStreak();
    int getKills();
    int getDeaths();
    void setKills(int kills);
    void setDeaths(int deaths);
    int increaseKills();
    int increaseDeaths();
    int decreaseKills();
    int decreaseDeaths();
}
