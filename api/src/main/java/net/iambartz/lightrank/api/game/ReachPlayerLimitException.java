package net.iambartz.lightrank.api.game;

public class ReachPlayerLimitException extends Exception {
    public ReachPlayerLimitException(String message) {
        super(message);
    }
}
