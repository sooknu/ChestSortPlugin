// File: SettingsGUIListener.java
package sooknu.chestsort;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class SettingsGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView() == null || !event.getView().getTitle().equals(SettingsGUI.GUI_TITLE)) {
            return;
        }
        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        PlayerSettings settings = PlayerSettings.get(player.getUniqueId());
        switch (slot) {
            // Core toggles.
            case 3:
                settings.setShiftClickSortEnabled(!settings.isShiftClickSortEnabled());
                player.sendMessage(ChatColor.YELLOW + "Shift+Click Auto Sort is now " +
                        (settings.isShiftClickSortEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                break;
            case 5:
                settings.setAutoContainerSortEnabled(!settings.isAutoContainerSortEnabled());
                player.sendMessage(ChatColor.YELLOW + "Auto Chest Sorting is now " +
                        (settings.isAutoContainerSortEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                break;
            // Additional hotkey toggles.
            case 19:
                settings.setLeftClickOutsideToContainer(!settings.isLeftClickOutsideToContainer());
                player.sendMessage(ChatColor.YELLOW + "Left-Click Outside is now " +
                        (settings.isLeftClickOutsideToContainer() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                break;
            case 21:
                settings.setRightClickOutsideToInventory(!settings.isRightClickOutsideToInventory());
                player.sendMessage(ChatColor.YELLOW + "Right-Click Outside is now " +
                        (settings.isRightClickOutsideToInventory() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                break;
            case 23:
                settings.setDoubleLeftClickOutside(!settings.isDoubleLeftClickOutside());
                player.sendMessage(ChatColor.YELLOW + "Double-Left-Click Outside is now " +
                        (settings.isDoubleLeftClickOutside() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                break;
            case 25:
                settings.setDoubleRightClickOutside(!settings.isDoubleRightClickOutside());
                player.sendMessage(ChatColor.YELLOW + "Double-Right-Click Outside is now " +
                        (settings.isDoubleRightClickOutside() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                break;
            default:
                return;
        }
        // Play GUI toggle sound.
        ChestSortPlugin plugin = ChestSortPlugin.getInstance();
        if (plugin.getConfig().getBoolean("gui-toggle-sound-enabled", true)) {
            try {
                String soundStr = plugin.getConfig().getString("gui-toggle-sound", "ITEM_ARMOR_EQUIP_CHAIN");
                float volume = (float) plugin.getConfig().getDouble("gui-toggle-sound-volume", 1.0);
                float pitch = (float) plugin.getConfig().getDouble("gui-toggle-sound-pitch", 1.0);
                Sound guiSound = Sound.valueOf(soundStr);
                player.playSound(player.getLocation(), guiSound, volume, pitch);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid gui-toggle-sound in config.yml: " + plugin.getConfig().getString("gui-toggle-sound"));
            }
        }
        // Refresh the GUI.
        SettingsGUI.open(player, ChestSortPlugin.getInstance());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView() != null && event.getView().getTitle().equals(SettingsGUI.GUI_TITLE)) {
            event.setCancelled(true);
        }
    }
}
