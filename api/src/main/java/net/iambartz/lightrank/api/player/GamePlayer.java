package net.iambartz.lightrank.api.player;

import net.iambartz.lightrank.api.game.Game;
import net.iambartz.lightrank.api.statistics.Statistics;

public interface GamePlayer<T extends Statistics> extends PlayerCredentials, PlayerSession {
    Game getGame();
}
