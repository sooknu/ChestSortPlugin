// File: SettingsGUI.java
package sooknu.chestsort;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SettingsGUI {
    // Title: Bold Black text.
    public static final String GUI_TITLE = ChatColor.BOLD + "" + ChatColor.BLACK + "ChestSort Settings";

    public static void open(Player player, ChestSortPlugin plugin) {
        // Create a 4x9 (36-slot) inventory.
        Inventory gui = Bukkit.createInventory(null, 36, GUI_TITLE);
        PlayerSettings settings = PlayerSettings.get(player.getUniqueId());

        // --- Core toggles (Row 0) ---
        // Slot 3: Shift+Click Auto Sort.
        gui.setItem(3, createToggleItem("Shift+Click Auto Sort", 
                settings.isShiftClickSortEnabled(), 
                "Shift-click an empty slot to auto sort", 
                settings.isShiftClickSortEnabled() ? Material.LIME_WOOL : Material.RED_WOOL));
        // Slot 5: Auto Chest Sorting.
        gui.setItem(5, createToggleItem("Auto Chest Sorting", 
                settings.isAutoContainerSortEnabled(), 
                "Automatically sort chests on close", 
                settings.isAutoContainerSortEnabled() ? Material.LIME_WOOL : Material.RED_WOOL));

        // --- Spacer Row (Row 1: slots 9-17 left empty) ---

        // --- Additional Hotkey toggles (Row 2) ---
        // Center them: slots 19, 21, 23, and 25.
        gui.setItem(19, createToggleItem("Left-Click Outside", 
                settings.isLeftClickOutsideToContainer(), 
                "Transfers matching items (except hotbar) to chest", 
                settings.isLeftClickOutsideToContainer() ? Material.LIME_WOOL : Material.RED_WOOL));
        gui.setItem(21, createToggleItem("Right-Click Outside", 
                settings.isRightClickOutsideToInventory(), 
                "Transfers matching items from chest to inventory", 
                settings.isRightClickOutsideToInventory() ? Material.LIME_WOOL : Material.RED_WOOL));
        gui.setItem(23, createToggleItem("Double-Left-Click Outside", 
                settings.isDoubleLeftClickOutside(), 
                "Transfers all items (except hotbar) to chest", 
                settings.isDoubleLeftClickOutside() ? Material.LIME_WOOL : Material.RED_WOOL));
        gui.setItem(25, createToggleItem("Double-Right-Click Outside", 
                settings.isDoubleRightClickOutside(), 
                "Transfers all items from chest into inventory", 
                settings.isDoubleRightClickOutside() ? Material.LIME_WOOL : Material.RED_WOOL));

        player.openInventory(gui);
    }

    private static ItemStack createToggleItem(String featureName, boolean enabled, String description, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + featureName);
        String stateLine = enabled ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";
        String descLine = ChatColor.GRAY + description;
        meta.setLore(Arrays.asList(stateLine, descLine));
        item.setItemMeta(meta);
        return item;
    }
}
