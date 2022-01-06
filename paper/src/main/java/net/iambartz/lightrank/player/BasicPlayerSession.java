package net.iambartz.lightrank.player;

import com.google.common.base.Objects;
import net.iambartz.lightrank.LightColor;
import net.iambartz.lightrank.api.invite.ExpiringInvite;
import net.iambartz.lightrank.api.invite.Invite;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.api.statistics.Statistics;
import net.iambartz.lightrank.game.Games;
import net.iambartz.lightrank.statistics.BasicStatistics;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class BasicPlayerSession implements PlayerSession {
    private final UUID          uniqueId;
    private final String        playerName;
    private final Statistics    statistics;
    private final Set<Invite> invites;
    private String currentSessionId;

    public BasicPlayerSession(Player player) {
        this(player, new BasicStatistics());
    }

    public BasicPlayerSession(Player player, Statistics statistics) {
        this.uniqueId = player.getUniqueId();
        this.playerName = player.getName();
        this.statistics = statistics;
        this.invites = new HashSet<>();
        this.currentSessionId = Games.NONE;
    }

    public BasicPlayerSession(UUID uniqueId, String playerName) {
        this(uniqueId, playerName, new BasicStatistics());
    }

    public BasicPlayerSession(UUID uniqueId, String playerName, Statistics statistics) {
        this.uniqueId = uniqueId;
        this.playerName = playerName;
        this.statistics = statistics;
        this.invites = new HashSet<>();
        this.currentSessionId = Games.NONE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicPlayerSession that = (BasicPlayerSession) o;
        return Objects.equal(uniqueId, that.uniqueId) && Objects.equal(playerName, that.playerName) && Objects.equal(statistics, that.statistics) && Objects.equal(currentSessionId, that.currentSessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uniqueId, playerName, statistics, currentSessionId);
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getPlayerName() {
        return this.playerName;
    }

    @Override
    public Statistics getStatistics() {
        return this.statistics;
    }

    @Override
    public String getCurrentSessionId() {
        return this.currentSessionId;
    }

    @Override
    public void setCurrentSessionId(String sessionId) {
        this.currentSessionId = sessionId;
    }

    @Override
    public boolean isPlaying() {
        return this.currentSessionId != null && this.currentSessionId != Games.NONE;
    }

    @Override
    public void message(String text) {
        Bukkit.getPlayer(this.uniqueId).sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    }

    @Override
    public void clickMessage(String text, String command) {
//        TextComponent textComponent = Component.text(ChatColor.translateAlternateColorCodes('&', text)).clickEvent(ClickEvent.runCommand("/" + command));
//        Bukkit.getPlayer(this.uniqueId).sendMessage(textComponent);
    }

    @Override
    public Set<Invite> getInvites() {
        return this.invites;
    }

    @Override
    public boolean containsInviteFrom(String playerName) {
        return this.invites.stream().anyMatch(invite -> invite.getSender().getPlayerName().equalsIgnoreCase(playerName));
    }

    @Override
    public void addInvite(Invite invite) {
        this.invites.add(invite);
//        final String sender = invite.getSender().getPlayerName();
//        TextComponent textComponent = Component.text(LightColor.INVITE_BASE + "Received new invite from " + LightColor.INVITE_KEY + sender)
//                .append(Component.text(LightColor.INVITE_ACC + " [Accept] ").clickEvent(ClickEvent.runCommand("/duel accept " + sender)))
//                .append(Component.text(LightColor.INVITE_DEN + "[Deny]").clickEvent(ClickEvent.runCommand("/duel deny " + sender)));
//        Bukkit.getPlayer(this.uniqueId).sendMessage(textComponent);
//        this.clearExpiredInvites();
    }

    @Override
    public void removeInvite(Invite invite) {
        this.invites.remove(invite);
        this.clearExpiredInvites();
    }

    @Override
    public void clearExpiredInvites() {
        this.invites.removeIf(invite -> {
            if (invite instanceof ExpiringInvite) {
                return ((ExpiringInvite) invite).hasExpired();
            }
            return false;
        });
    }
}
