// File: InventoryCloseListener.java
package sooknu.chestsort;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryCloseListener implements Listener {

    private final ChestSortPlugin plugin;

    public InventoryCloseListener(ChestSortPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if (inv == null) return;
        
        // Only process container inventories (ignore player's own inventory).
        if (inv.equals(((Player) event.getPlayer()).getInventory())) {
            return;
        }
        
        Player player = (Player) event.getPlayer();
        // Check if auto chest sorting is enabled for the player.
        if (PlayerSettings.get(player.getUniqueId()).isAutoContainerSortEnabled()) {
            InventorySorter.sortInventory(inv);
            // Only send message if messages are enabled.
            if (plugin.getConfig().getBoolean("messages.enabled", true)) {
                String autoSortMsg = ChatColor.translateAlternateColorCodes('&', 
                        plugin.getConfig().getString("messages.auto_container_sort", "&aContainer automatically sorted."));
                player.sendMessage(ChatColor.GREEN + autoSortMsg);
            }
        }
    }
}
