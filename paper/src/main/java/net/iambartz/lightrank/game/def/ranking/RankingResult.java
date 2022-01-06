package net.iambartz.lightrank.game.def.ranking;

import net.iambartz.lightrank.api.game.Game;
import net.iambartz.lightrank.api.game.GameResult;

public class RankingResult implements GameResult {
    private final String winner;
    private final int opponentRating;
    private final Game game;

    public RankingResult(String winner, int opponentRating, Game game) {
        this.winner = winner;
        this.opponentRating = opponentRating;
        this.game = game;
    }

    @Override
    public String[] getWinners() {
        return new String[]{this.winner};
    }

    public int getOpponentRating() {
        return this.opponentRating;
    }

    @Override
    public Game getGame() {
        return this.game;
    }
}
