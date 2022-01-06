package net.iambartz.lightrank.api.player;

import net.iambartz.lightrank.api.statistics.Statistics;
import org.bukkit.Bukkit;
import org.bukkit.inventory.PlayerInventory;

public interface KitPlayer<T extends Statistics> extends GamePlayer<T> {
    default PlayerInventory getInventory() {
        return Bukkit.getPlayer(this.getUniqueId()).getInventory();
    }
    void kit();
    void loadInventoryBefore();
}
