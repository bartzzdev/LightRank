package net.iambartz.lightrank.game.def.duel;

import com.google.common.base.Objects;
import net.iambartz.lightrank.api.invite.ExpiringInvite;
import net.iambartz.lightrank.api.player.PlayerSession;
import org.bukkit.Bukkit;

import java.time.LocalTime;

/**
 * A duel match invite, expires after 1 minute.
 */
public class DuelInvite implements ExpiringInvite {
    private final LocalTime timeReceived;
    private final PlayerSession sender;
    private final PlayerSession receiver;

    public DuelInvite(PlayerSession sender, PlayerSession receiver) {
        this.timeReceived = LocalTime.now();
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DuelInvite that = (DuelInvite) o;
        return Objects.equal(timeReceived, that.timeReceived) && Objects.equal(sender, that.sender);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timeReceived, sender);
    }

    @Override
    public LocalTime getTimeReceived() {
        return this.timeReceived;
    }

    @Override
    public boolean hasExpired() {
        return this.timeReceived.isAfter(this.timeReceived.plusMinutes(1));
    }

    @Override
    public PlayerSession getSender() {
        return this.sender;
    }

    @Override
    public void accept() {
        if (this.hasExpired()) {
            return;
        }

        if (this.sender.isPlaying()) {
            return;
        }

    }

    @Override
    public void deny() {
        this.receiver.removeInvite(this);
    }
}
