package net.iambartz.lightrank.game;

import net.iambartz.lightrank.api.game.Game;
import net.iambartz.lightrank.api.game.GameResult;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GamePostProcessEvent  extends Event {
    private static HandlerList  handlerList = new HandlerList();
    private final GameResult    result;
    private final Game          game;

    public GamePostProcessEvent(GameResult result, Game game) {
        this.result = result;
        this.game = game;
    }

    public GameResult getResult() {
        return this.result;
    }

    public Game getGame() {
        return this.game;
    }

    public static HandlerList getHandlerList() {
        return GamePostProcessEvent.handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return GamePostProcessEvent.handlerList;
    }
}
