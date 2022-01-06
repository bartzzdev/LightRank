package net.iambartz.lightrank.game;

import com.google.common.collect.ImmutableSet;
import net.iambartz.lightrank.api.player.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GameStartEvent extends Event {
    private static HandlerList handlerList = new HandlerList();
    private final Set<? extends GamePlayer> players;

    public GameStartEvent(Set<? extends GamePlayer> players) {
        this.players = ImmutableSet.copyOf(players);
    }

    public Set<? extends GamePlayer> getPlayers() {
        return this.players;
    }

    public static HandlerList getHandlerList() {
        return GameStartEvent.handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return GameStartEvent.handlerList;
    }
}
