package net.iambartz.lightrank.api.game;

public interface Countdown {
    boolean isFinished();
    boolean isCounting();
    void stop(Game game, boolean forceStop);
}
