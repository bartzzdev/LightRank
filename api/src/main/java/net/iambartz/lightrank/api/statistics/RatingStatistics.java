package net.iambartz.lightrank.api.statistics;

public interface RatingStatistics extends Statistics, KDStatistics {
    int calculateRating();
    int getLastRatingDifference();
}
