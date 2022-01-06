package net.iambartz.lightrank.api.game;

import net.iambartz.lightrank.api.player.GamePlayer;

public interface TeamGame<T extends GamePlayer> extends Game {
    @Override
    default void message(String text) {
        Game.super.message(text);
    }
}
