// File: ContainerAutoSortListener.java
package sooknu.chestsort;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.ChatColor;

public class ContainerAutoSortListener implements Listener {

    private final ChestSortPlugin plugin;

    public ContainerAutoSortListener(ChestSortPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Only process if the inventory holder is not a player (i.e. it's a container)
        Inventory inv = event.getInventory();
        if (inv == null || inv.getHolder() instanceof Player) {
            return;
        }
        // Check if the world is disabled.
        String worldName = event.getPlayer().getWorld().getName();
        if (plugin.getConfig().getStringList("disabled-worlds").contains(worldName)) {
            return;
        }
        // Check if auto-sorting for containers is enabled.
        if (plugin.getConfig().getBoolean("automatic-chest-sorting", false)) {
            InventorySorter.sortInventory(inv);
            if (event.getPlayer() instanceof Player) {
                ((Player) event.getPlayer()).sendMessage(ChatColor.GREEN + "Container automatically sorted.");
            }
        }
    }
}
