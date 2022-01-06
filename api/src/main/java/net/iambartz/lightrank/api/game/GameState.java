package net.iambartz.lightrank.api.game;

public enum GameState {
    STOPPED(0),
    STARTING(1),
    PLAYING(2),
    POST_PROCESSING(3),
    WAITING(4);

    private final int stateNumber;

    GameState(int i) {
        this.stateNumber = i;
    }

    public int stateNumber() {
        return this.stateNumber;
    }

    public boolean isPlaying() {
        return this.stateNumber == 2;
    }
}
