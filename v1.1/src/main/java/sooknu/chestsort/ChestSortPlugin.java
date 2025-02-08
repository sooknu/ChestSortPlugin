package sooknu.chestsort;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ChestSortPlugin extends JavaPlugin implements CommandExecutor, Listener {

    private final Map<UUID, Boolean> playerSortingStatus = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("chestsort").setExecutor(this);
        getLogger().info("ChestSort Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChestSort Plugin Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("chestsort")) {
            if (sender instanceof Player player) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("on")) {
                        playerSortingStatus.put(player.getUniqueId(), true);
                        player.sendMessage("Sorting enabled.");
                    } else if (args[0].equalsIgnoreCase("off")) {
                        playerSortingStatus.put(player.getUniqueId(), false);
                        player.sendMessage("Sorting disabled.");
                    } else {
                        player.sendMessage("Usage: /chestsort on|off");
                    }
                } else {
                    player.sendMessage("Usage: /chestsort on|off");
                }
            } else {
                sender.sendMessage("Only players can use this command.");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Check if sorting is enabled for the player
        if (!playerSortingStatus.getOrDefault(player.getUniqueId(), true)) return;

        // Only trigger sorting when shift-clicking on an empty slot
        if (event.isShiftClick() && (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)) {
            sortInventory(event.getClickedInventory(), player);
            event.setCancelled(true);
        }
    }

    private void sortInventory(Inventory inventory, Player player) {
        boolean isPlayerInventory = inventory.getHolder() instanceof Player;
        ItemStack[] contents = inventory.getContents();
        List<ItemStack> sortedItems = new ArrayList<>();

        // Collect items, ignoring hotbar (0-8) and armor (36-39) in player inventory
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];

            if (isPlayerInventory && (i >= 0 && i <= 8 || i >= 36 && i <= 39)) {
                continue;
            }

            if (item != null && item.getType() != Material.AIR) {
                sortedItems.add(item.clone());  // Preserve metadata and enchantments
                contents[i] = null;  // Clear original slot
            }
        }

        // Sort items with priority: Weapons > Tools > Armor > Blocks > Other
        sortedItems.sort(Comparator
                .comparingInt(this::getItemPriority)
                .thenComparing(item -> item.getType().name())
        );

        // Add items back into inventory, respecting stack limits and unstackable items
        int index = isPlayerInventory ? 9 : 0;
        for (ItemStack item : sortedItems) {
            if (item.getMaxStackSize() == 1) {
                // Unstackable item: add directly
                if (index < contents.length) {
                    contents[index++] = item;
                }
            } else {
                // Stackable item: stack up to 64
                index = addOrStackItem(contents, item, index, isPlayerInventory);
            }
        }

        inventory.setContents(contents);
    }

    private int addOrStackItem(ItemStack[] contents, ItemStack newItem, int index, boolean isPlayerInventory) {
        // Try to stack into existing similar items first
        for (int i = 0; i < contents.length; i++) {
            if (isPlayerInventory && (i >= 0 && i <= 8 || i >= 36 && i <= 39)) {
                continue;  // Skip hotbar and armor slots
            }
            ItemStack existingItem = contents[i];
            if (existingItem != null && existingItem.isSimilar(newItem)) {
                int totalAmount = existingItem.getAmount() + newItem.getAmount();
                if (totalAmount <= 64) {
                    existingItem.setAmount(totalAmount);
                    return index;
                } else {
                    existingItem.setAmount(64);
                    newItem.setAmount(totalAmount - 64);
                }
            }
        }

        // Place remaining items in empty slots
        for (; index < contents.length; index++) {
            if (isPlayerInventory && (index >= 0 && index <= 8 || index >= 36 && index <= 39)) {
                continue;
            }
            if (contents[index] == null) {
                contents[index] = newItem;
                break;
            }
        }
        return index + 1;
    }

    private int getItemPriority(ItemStack item) {
        Material type = item.getType();
        if (type.toString().endsWith("SWORD") || type.toString().endsWith("BOW") || type.toString().endsWith("CROSSBOW") || type.toString().endsWith("TRIDENT")) return 1; // Weapons
        if (type.toString().endsWith("PICKAXE") || type.toString().endsWith("AXE") || type.toString().endsWith("SHOVEL") || type.toString().endsWith("HOE") || type == Material.SHEARS || type == Material.FLINT_AND_STEEL) return 2; // Tools
        if (type.toString().endsWith("HELMET") || type.toString().endsWith("CHESTPLATE") || type.toString().endsWith("LEGGINGS") || type.toString().endsWith("BOOTS") || type == Material.SHIELD) return 3; // Armor
        if (type.isBlock()) return 4; // Blocks
        return 5; // Other items
    }
}
