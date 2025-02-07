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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class ChestSortPlugin extends JavaPlugin implements CommandExecutor {

    private final Map<UUID, Boolean> playerSortingEnabled = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        this.getCommand("chestsort").setExecutor(this);
        getLogger().info("ChestSort Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChestSort Plugin Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length != 1 || !(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off"))) {
            player.sendMessage("Usage: /chestsort on|off");
            return true;
        }

        boolean enable = args[0].equalsIgnoreCase("on");
        playerSortingEnabled.put(player.getUniqueId(), enable);
        player.sendMessage(enable ? "Shift+Click sorting enabled." : "Shift+Click sorting disabled.");

        return true;
    }

    public boolean isSortingEnabled(Player player) {
        return playerSortingEnabled.getOrDefault(player.getUniqueId(), true);
    }

    private class InventoryListener implements Listener {

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();

            if (!isSortingEnabled(player)) return;

            if (event.isShiftClick() && event.getClickedInventory() != null) {
                // Trigger sorting only if clicking on an empty slot
                if (event.getClickedInventory().getItem(event.getSlot()) != null) return;

                if (event.getClickedInventory().getType() == InventoryType.CHEST || 
                    event.getClickedInventory().getType() == InventoryType.BARREL || 
                    event.getClickedInventory().getType() == InventoryType.SHULKER_BOX) {
                    sortInventory(event.getClickedInventory());
                    event.setCancelled(true);
                } else if (event.getClickedInventory().equals(player.getInventory())) {
                    sortPlayerInventory(player.getInventory());
                    event.setCancelled(true);
                }
            }
        }

        private void sortInventory(Inventory inventory) {
            List<ItemStack> items = Arrays.stream(inventory.getContents())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Map<Material, ItemStack> stackedItems = new HashMap<>();
            for (ItemStack item : items) {
                Material type = item.getType();
                if (stackedItems.containsKey(type)) {
                    int totalAmount = stackedItems.get(type).getAmount() + item.getAmount();
                    stackedItems.get(type).setAmount(Math.min(totalAmount, 64));
                } else {
                    stackedItems.put(type, item.clone());
                }
            }

            List<ItemStack> sortedItems = new ArrayList<>(stackedItems.values());
            sortedItems.sort(Comparator.comparingInt(this::getItemPriority).thenComparing(item -> item.getType().name()));

            inventory.clear();
            for (ItemStack item : sortedItems) {
                inventory.addItem(item);
            }
        }

        private void sortPlayerInventory(Inventory inventory) {
            ItemStack[] contents = inventory.getContents();
            List<ItemStack> itemsToSort = new ArrayList<>();

            for (int i = 9; i < 36; i++) {
                if (contents[i] != null && !isArmor(contents[i])) {
                    itemsToSort.add(contents[i]);
                    contents[i] = null;
                }
            }

            Map<Material, ItemStack> stackedItems = new HashMap<>();
            for (ItemStack item : itemsToSort) {
                Material type = item.getType();
                if (stackedItems.containsKey(type)) {
                    int totalAmount = stackedItems.get(type).getAmount() + item.getAmount();
                    stackedItems.get(type).setAmount(Math.min(totalAmount, 64));
                } else {
                    stackedItems.put(type, item.clone());
                }
            }

            List<ItemStack> sortedItems = new ArrayList<>(stackedItems.values());
            sortedItems.sort(Comparator.comparingInt(this::getItemPriority).thenComparing(item -> item.getType().name()));

            int index = 9;
            for (ItemStack item : sortedItems) {
                contents[index++] = item;
            }

            inventory.setContents(contents);
        }

        private boolean isArmor(ItemStack item) {
            Material type = item.getType();
            return type.toString().endsWith("HELMET") || 
                   type.toString().endsWith("CHESTPLATE") || 
                   type.toString().endsWith("LEGGINGS") || 
                   type.toString().endsWith("BOOTS") || 
                   type == Material.SHIELD;
        }

        private boolean isWeapon(ItemStack item) {
            Material type = item.getType();
            return type.toString().endsWith("SWORD") || 
                   type.toString().endsWith("BOW") || 
                   type.toString().endsWith("CROSSBOW") || 
                   type == Material.TRIDENT;
        }

        private boolean isTool(ItemStack item) {
            Material type = item.getType();
            return type.toString().endsWith("PICKAXE") || 
                   type.toString().endsWith("AXE") || 
                   type.toString().endsWith("SHOVEL") || 
                   type.toString().endsWith("HOE") || 
                   type == Material.SHEARS;
        }

        private int getItemPriority(ItemStack item) {
            if (isWeapon(item)) return 1;
            if (isTool(item)) return 2;
            if (isArmor(item)) return 3;
            if (item.getType().isBlock()) return 4;
            return 5;
        }
    }
}
