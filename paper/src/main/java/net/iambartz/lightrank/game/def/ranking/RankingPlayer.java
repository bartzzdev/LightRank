package net.iambartz.lightrank.game.def.ranking;

import net.iambartz.lightrank.api.game.Game;
import net.iambartz.lightrank.api.invite.Invite;
import net.iambartz.lightrank.api.player.KitPlayer;
import net.iambartz.lightrank.api.player.PlayerSession;
import net.iambartz.lightrank.api.statistics.Statistics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Set;
import java.util.UUID;

public class RankingPlayer implements KitPlayer<RankingStatistics> {
    private final PlayerSession playerSession;
    private final RankingStatistics statistics;
    private final ItemStack[] inventory;
    private Game game;

    public RankingPlayer(PlayerSession session, Game game) {
        this.playerSession = session;
        this.statistics = new RankingStatistics();
        this.game = game;
        this.inventory = this.getInventory().getContents();
    }

    public RankingPlayer(PlayerSession session) {
        this(session, null);
    }

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public void kit() {
        this.getInventory().clear();
        this.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        this.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        this.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        this.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        this.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
        this.getInventory().setItem(1, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 2));
        this.getInventory().setItem(2, new ItemStack(Material.GOLDEN_APPLE, 5));
    }

    @Override
    public void loadInventoryBefore() {
        this.getInventory().clear();
        this.getInventory().setContents(this.inventory);
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
    public RankingStatistics getStatistics() {
        return this.statistics;
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
        return this.game != null;
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
    public Set<Invite> getInvites() {
        return null;
    }

    @Override
    public boolean containsInviteFrom(String playerName) {
        return false;
    }

    @Override
    public void addInvite(Invite invite) {

    }

    @Override
    public void removeInvite(Invite invite) {

    }

    @Override
    public void clearExpiredInvites() {

    }
}
