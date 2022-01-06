package net.iambartz.lightrank.game.def.ranking;

import net.iambartz.lightrank.api.statistics.RatingStatistics;

public final class RankingStatistics implements RatingStatistics {
    private int kills, deaths;
    private int rating;
    private int killsStreak, deathsStreak;
    private int gamesPlayed;
    private int totalOpponentRatings;

    public RankingStatistics() {
        this.rating = 1000;
    }

    public RankingStatistics(int kills, int deaths, int rating, int killsStreak, int deathsStreak, int gamesPlayed, int totalOpponentRatings) {
        this.kills = kills;
        this.deaths = deaths;
        this.rating = rating;
        this.killsStreak = killsStreak;
        this.deathsStreak = deathsStreak;
        this.gamesPlayed = gamesPlayed;
        this.totalOpponentRatings = 0;
    }

    @Override
    public double getKDRatio() {
        return kills/deaths;
    }

    @Override
    public int getKillsStreak() {
        return this.killsStreak;
    }

    @Override
    public int getDeathsStreak() {
        return this.deathsStreak;
    }

    @Override
    public int getKills() {
        return this.kills;
    }

    @Override
    public int getDeaths() {
        return this.deaths;
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;
    }

    @Override
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @Override
    public int increaseKills() {
        return ++this.kills;
    }

    @Override
    public int increaseDeaths() {
        return ++this.deaths;
    }

    @Override
    public int decreaseKills() {
        return --this.kills;
    }

    @Override
    public int decreaseDeaths() {
        return --this.deaths;
    }

    @Override
    public int calculateRating() {
        if (this.gamesPlayed == 0) {
            return 1000;
        }
        return (this.totalOpponentRatings + (400 * (this.kills - this.deaths))) / this.gamesPlayed;
    }

    @Override
    public int getLastRatingDifference() {
        return 0;
    }

    public void addOpponentRating(int rating) {
        this.totalOpponentRatings += rating;
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
