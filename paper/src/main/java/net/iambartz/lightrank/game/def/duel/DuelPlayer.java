package net.iambartz.lightrank.game.def.duel;

import com.google.common.base.Objects;
import net.iambartz.lightrank.api.game.Game;
import net.iambartz.lightrank.api.invite.Invite;
import net.iambartz.lightrank.api.player.KitPlayer;
import net.iambartz.lightrank.api.player.PlayerSession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Set;
import java.util.UUID;

public class DuelPlayer implements KitPlayer<DuelStatistics> {
    private final PlayerSession     playerSession;
    private final DuelStatistics    duelStatistics;
    private final Game              game;
    private final ItemStack[]       playerInventory;

    public DuelPlayer(PlayerSession playerSession, Game game) {
        this.playerSession = playerSession;
        this.duelStatistics = new DuelStatistics(playerSession.getStatistics());
        this.game = game;
        this.playerInventory = this.getInventory().getContents();
    }

    public DuelPlayer(PlayerSession playerSession, DuelStatistics duelStatistics, Game game) {
        this.playerSession = playerSession;
        this.duelStatistics = duelStatistics;
        this.game = game;
        this.playerInventory = this.getInventory().getContents();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DuelPlayer that = (DuelPlayer) o;
        return Objects.equal(playerSession, that.playerSession) && Objects.equal(duelStatistics, that.duelStatistics) && Objects.equal(game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerSession, duelStatistics, game);
    }

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public UUID getUniqueId() {
        return this.playerSession.getUniqueId();
    }

    @Override
    public String getPlayerName() {
        return this.playerSession.getPlayerName();
    }

    @Override
    public DuelStatistics getStatistics() {
        return this.duelStatistics;
    }

    @Override
    public String getCurrentSessionId() {
        return this.playerSession.getCurrentSessionId();
    }

    @Override
    public void setCurrentSessionId(String sessionId) {
        this.playerSession.setCurrentSessionId(sessionId);
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void message(String text) {
        this.playerSession.message(text);
    }

    @Override
    public void clickMessage(String text, String command) {
        this.playerSession.clickMessage(text, command);
    }

    @Override
    public PlayerInventory getInventory() {
        return Bukkit.getPlayer(this.getUniqueId()).getInventory();
    }

    @Override
    public void kit() {
        this.getInventory().clear();
        this.getInventory().setItem(EquipmentSlot.HAND, new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public void loadInventoryBefore() {
        this.getInventory().clear();
        this.getInventory().setContents(this.playerInventory);
    }

    @Override
    public Set<Invite> getInvites() {
        return this.playerSession.getInvites();
    }

    @Override
    public boolean containsInviteFrom(String playerName) {
        return false;
    }

    @Override
    public void addInvite(Invite invite) {
        this.playerSession.addInvite(invite);
    }

    @Override
    public void removeInvite(Invite invite) {
        this.playerSession.removeInvite(invite);
    }

    @Override
    public void clearExpiredInvites() {
        this.playerSession.clearExpiredInvites();
    }
}
