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

    private boolean shiftClickEnabled = true;

    // =========================
    // Plugin Lifecycle Methods
    // =========================
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

    // =========================
    // Command Handling
    // =========================
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                shiftClickEnabled = true;
                sender.sendMessage("Shift+Click sorting enabled.");
            } else if (args[0].equalsIgnoreCase("off")) {
                shiftClickEnabled = false;
                sender.sendMessage("Shift+Click sorting disabled.");
            } else {
                sender.sendMessage("Usage: /chestsort on|off");
            }
        } else {
            sender.sendMessage("Usage: /chestsort on|off");
        }
        return true;
    }

    // =========================
    // Inventory Click Handling
    // =========================
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Keep vanilla behavior when shift-clicking on an item
        if (event.isShiftClick() && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
            return; // Vanilla behavior
        }

        // Trigger sorting ONLY if shift-clicking on an empty slot
        if (shiftClickEnabled && event.isShiftClick() &&
            (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)) {

            Inventory clickedInventory = event.getClickedInventory();

            if (clickedInventory != null) {
                sortInventory(clickedInventory);
                event.setCancelled(true);
            }
        }
    }

    // =========================
    // Sorting Logic for Inventories
    // =========================
    private void sortInventory(Inventory inventory) {
        Map<String, ItemStack> itemMap = new LinkedHashMap<>();

        // Count total items, keeping metadata intact
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                String key = item.getType().toString() + ":" + item.getItemMeta().toString();
                if (itemMap.containsKey(key)) {
                    ItemStack existingItem = itemMap.get(key);
                    int newAmount = existingItem.getAmount() + item.getAmount();
                    existingItem.setAmount(Math.min(64, newAmount));
                    if (newAmount > 64) {
                        ItemStack extraStack = existingItem.clone();
                        extraStack.setAmount(newAmount - 64);
                        itemMap.put(key + UUID.randomUUID(), extraStack);  // Unique key for extra stacks
                    }
                } else {
                    itemMap.put(key, item.clone());
                }
            }
        }

        List<ItemStack> sortedItems = new ArrayList<>(itemMap.values());

        // Sort based on priority (Weapons, Tools, Armor, Blocks)
        sortedItems.sort(Comparator.comparingInt(this::getItemPriority).thenComparing(item -> item.getType().name()));

        inventory.clear();
        for (ItemStack item : sortedItems) {
            inventory.addItem(item);
        }
    }

    // =========================
    // Utility Methods
    // =========================
    private boolean isArmor(ItemStack item) {
        Material type = item.getType();
        return type.toString().endsWith("HELMET") ||
               type.toString().endsWith("CHESTPLATE") ||
               type.toString().endsWith("LEGGINGS") ||
               type.toString().endsWith("BOOTS") ||
               type == Material.SHIELD;
    }

    private int getItemPriority(ItemStack item) {
        Material type = item.getType();
        if (type.toString().endsWith("SWORD") ||
            type.toString().endsWith("AXE") ||
            type.toString().endsWith("BOW") ||
            type.toString().endsWith("CROSSBOW") ||
            type.toString().endsWith("TRIDENT")) return 1;

        if (type.toString().endsWith("PICKAXE") ||
            type.toString().endsWith("SHOVEL") ||
            type.toString().endsWith("HOE") ||
            type == Material.SHEARS ||
            type == Material.FLINT_AND_STEEL) return 2;

        if (isArmor(item)) return 3;
        if (type.isBlock()) return 4;
        return 5;
    }
}
