// File: InventoryClickListener.java
package sooknu.chestsort;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class InventoryClickListener implements Listener {

    private final ChestSortPlugin plugin;
    // Maps for outside click scheduling.
    private static final Map<String, BukkitTask> leftClickTaskMap = new HashMap<>();
    private static final Map<String, BukkitTask> rightClickTaskMap = new HashMap<>();
    // Delay in ticks (configured from config)
    private final long DOUBLE_CLICK_DELAY_TICKS;

    public InventoryClickListener(ChestSortPlugin plugin) {
        this.plugin = plugin;
        this.DOUBLE_CLICK_DELAY_TICKS = plugin.getConfig().getLong("hotkeys.double_click_delay_ticks", 6L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Allow vanilla drop behavior for drop and control-drop clicks in player inventory.
        if (event.getClickedInventory() != null 
                && event.getClickedInventory().equals(((Player) event.getWhoClicked()).getInventory())) {
            if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
                return;
            }
        }
        
        // Cancel processing if the settings GUI is open.
        if (event.getView() != null && event.getView().getTitle().equals(SettingsGUI.GUI_TITLE)) {
            event.setCancelled(true);
            return;
        }
        
        // Core shift-click sorting.
        if (event.isShiftClick()) {
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem != null && currentItem.getType() != Material.AIR)
                return;
            if (!(event.getWhoClicked() instanceof Player))
                return;
            Player player = (Player) event.getWhoClicked();
            if (!PlayerSettings.get(player.getUniqueId()).isShiftClickSortEnabled())
                return;
            event.setCancelled(true);
            Inventory inv = event.getClickedInventory();
            if (inv == null)
                return;
            if (inv.equals(player.getInventory())) {
                InventorySorter.sortPlayerInventory(player);
            } else {
                InventorySorter.sortInventory(inv);
            }
            String sortMessage = ChatColor.translateAlternateColorCodes('&', 
                    plugin.getConfig().getString("messages.sort_complete", "&aSorted inventory!"));
            if (plugin.getConfig().getBoolean("messages.enabled", true)) {
                player.sendMessage(sortMessage);
            }
            if (plugin.getConfig().getBoolean("sound-on-sort", true)) {
                try {
                    String soundStr = plugin.getConfig().getString("sort-sound", "UI_BUTTON_CLICK");
                    float volume = (float) plugin.getConfig().getDouble("sort-sound-volume", 1.0);
                    float pitch = (float) plugin.getConfig().getDouble("sort-sound-pitch", 1.0);
                    Sound sortSound = Sound.valueOf(soundStr);
                    player.playSound(player.getLocation(), sortSound, volume, pitch);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid sort-sound in config.yml: " 
                            + plugin.getConfig().getString("sort-sound"));
                }
            }
            return;
        }
        
        // Debug Section: Uncomment for debugging outside click details.
        /*
        if (event.getRawSlot() == -999) {
            Player player = (Player) event.getWhoClicked();
            Inventory topInv = event.getView().getTopInventory();
            plugin.getLogger().info("DEBUG: Outside click: type=" + event.getClick() + ", inventoryType=" + topInv.getType().toString());
        }
        */
        
        // Handle outside clicks (raw slot == -999).
        if (event.getRawSlot() == -999) {
            if (!(event.getWhoClicked() instanceof Player))
                return;
            final Player player = (Player) event.getWhoClicked();
            final Inventory topInv = event.getView().getTopInventory();
            // If the top inventory is the player's own inventory (type CRAFTING), let vanilla behavior occur.
            if (topInv.getType().toString().equals("CRAFTING")) {
                return;
            }
            final PlayerSettings ps = PlayerSettings.get(player.getUniqueId());
            
            // Handle LEFT clicks outside with scheduling.
            if (event.getClick() == ClickType.LEFT) {
                event.setCancelled(true);
                final String uuid = player.getUniqueId().toString();
                if (leftClickTaskMap.containsKey(uuid)) {
                    leftClickTaskMap.get(uuid).cancel();
                    leftClickTaskMap.remove(uuid);
                    if (ps.isDoubleLeftClickOutside()) {
                        InventorySorter.transferAllToContainer(topInv, player);
                        String msg = ChatColor.translateAlternateColorCodes('&', 
                                plugin.getConfig().getString("messages.transfer_all_to_container", "&aTransferred ALL items from inventory into container."));
                        if (plugin.getConfig().getBoolean("messages.enabled", true)) {
                            player.sendMessage(ChatColor.GREEN + msg);
                        }
                    }
                } else {
                    BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        if (ps.isLeftClickOutsideToContainer()) {
                            InventorySorter.transferMatchingToContainer(topInv, player);
                            String msg = ChatColor.translateAlternateColorCodes('&', 
                                plugin.getConfig().getString("messages.transfer_matching_to_container", "&aTransferred matching items to chest."));
                            if (plugin.getConfig().getBoolean("messages.enabled", true)) {
                                player.sendMessage(ChatColor.GREEN + msg);
                            }
                        }
                        leftClickTaskMap.remove(uuid);
                    }, DOUBLE_CLICK_DELAY_TICKS);
                    leftClickTaskMap.put(uuid, task);
                }
            }
            // Handle RIGHT clicks outside with scheduling.
            else if (event.getClick() == ClickType.RIGHT) {
                event.setCancelled(true);
                final String uuid = player.getUniqueId().toString();
                if (rightClickTaskMap.containsKey(uuid)) {
                    rightClickTaskMap.get(uuid).cancel();
                    rightClickTaskMap.remove(uuid);
                    if (ps.isDoubleRightClickOutside()) {
                        InventorySorter.transferAllToInventory(topInv, player);
                        String msg = ChatColor.translateAlternateColorCodes('&', 
                                plugin.getConfig().getString("messages.transfer_all_to_inventory", "&aTransferred ALL items from chest into inventory."));
                        if (plugin.getConfig().getBoolean("messages.enabled", true)) {
                            player.sendMessage(ChatColor.GREEN + msg);
                        }
                    }
                } else {
                    BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        if (ps.isRightClickOutsideToInventory()) {
                            InventorySorter.transferMatchingToInventory(topInv, player);
                            String msg = ChatColor.translateAlternateColorCodes('&', 
                                plugin.getConfig().getString("messages.transfer_matching_to_inventory", "&aTransferred matching items from chest to inventory."));
                            if (plugin.getConfig().getBoolean("messages.enabled", true)) {
                                player.sendMessage(ChatColor.GREEN + msg);
                            }
                        }
                        rightClickTaskMap.remove(uuid);
                    }, DOUBLE_CLICK_DELAY_TICKS);
                    rightClickTaskMap.put(uuid, task);
                }
            }
            return;
        }
    }
}
