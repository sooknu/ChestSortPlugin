// File: InventorySorter.java
package sooknu.chestsort;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventorySorter {

    // Sorts a generic inventory (for containers).
    public static void sortInventory(Inventory inv) {
        if (inv == null) return;
        
        // Check if sorting is enabled for this inventory type (using the config).
        // The default is true if not specified.
        if (!ChestSortPlugin.getInstance().getConfig().getBoolean("sorting_enabled." + inv.getType().toString(), true)) {
            return;
        }
        
        List<Integer> allowedSlots = new ArrayList<>();
        for (int i = 0; i < inv.getSize(); i++) {
            allowedSlots.add(i);
        }
        List<ItemStack> items = new ArrayList<>();
        for (int slot : allowedSlots) {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                items.add(item.clone());
            }
            inv.setItem(slot, null);
        }
        items.sort(new ItemStackComparator());
        List<ItemStack> mergedItems = mergeStacks(items);
        int index = 0;
        for (ItemStack item : mergedItems) {
            if (index < allowedSlots.size()) {
                inv.setItem(allowedSlots.get(index), item);
                index++;
            }
        }
    }

    // Sorts only the main part of a player's inventory (slots 9-35).
    public static void sortPlayerInventory(Player player) {
        Inventory inv = player.getInventory();
        int start = 9;
        int end = 36;  // Slots 9 to 35 (main inventory)
        List<Integer> allowedSlots = new ArrayList<>();
        for (int i = start; i < end; i++) {
            allowedSlots.add(i);
        }
        List<ItemStack> items = new ArrayList<>();
        for (int slot : allowedSlots) {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                items.add(item.clone());
            }
            inv.setItem(slot, null);
        }
        items.sort(new ItemStackComparator());
        List<ItemStack> mergedItems = mergeStacks(items);
        int index = 0;
        for (ItemStack item : mergedItems) {
            if (index < allowedSlots.size()) {
                inv.setItem(allowedSlots.get(index), item);
                index++;
            }
        }
    }

    private static List<ItemStack> mergeStacks(List<ItemStack> items) {
        List<ItemStack> merged = new ArrayList<>();
        for (ItemStack item : items) {
            if (item.getMaxStackSize() == 1) {
                merged.add(item);
                continue;
            }
            boolean mergedFlag = false;
            for (ItemStack current : merged) {
                if (current.getType() == item.getType()) {
                    int total = current.getAmount() + item.getAmount();
                    int max = item.getMaxStackSize();
                    if (total <= max) {
                        current.setAmount(total);
                        mergedFlag = true;
                        break;
                    } else {
                        current.setAmount(max);
                        int remainder = total - max;
                        item.setAmount(remainder);
                    }
                }
            }
            if (!mergedFlag) {
                merged.add(item);
            }
        }
        return merged;
    }

    // Transfers matching items from player's main inventory (slots 9-35) into the container.
    public static void transferMatchingToContainer(Inventory container, Player player) {
        Inventory playerInv = player.getInventory();
        for (int i = 9; i < 36; i++) {
            ItemStack item = playerInv.getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            boolean found = false;
            for (int j = 0; j < container.getSize(); j++) {
                ItemStack contItem = container.getItem(j);
                if (contItem != null && contItem.getType() == item.getType()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                Map<Integer, ItemStack> leftovers = container.addItem(item);
                if (leftovers.isEmpty()) {
                    playerInv.setItem(i, null);
                } else {
                    playerInv.setItem(i, leftovers.values().iterator().next());
                }
            }
        }
    }

    // Transfers matching items from the container into player's main inventory (slots 9-35).
    public static void transferMatchingToInventory(Inventory container, Player player) {
        Inventory playerInv = player.getInventory();
        for (int i = 0; i < container.getSize(); i++) {
            ItemStack item = container.getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            boolean found = false;
            for (int j = 9; j < 36; j++) {
                ItemStack pItem = playerInv.getItem(j);
                if (pItem != null && pItem.getType() == item.getType()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                Map<Integer, ItemStack> leftovers = playerInv.addItem(item);
                if (leftovers.isEmpty()) {
                    container.setItem(i, null);
                } else {
                    container.setItem(i, leftovers.values().iterator().next());
                }
            }
        }
    }

    // Transfers ALL items from player's main inventory (slots 9-35) into the container.
    public static void transferAllToContainer(Inventory container, Player player) {
        Inventory playerInv = player.getInventory();
        for (int i = 9; i < 36; i++) {
            ItemStack item = playerInv.getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            Map<Integer, ItemStack> leftovers = container.addItem(item);
            if (leftovers.isEmpty()) {
                playerInv.setItem(i, null);
            } else {
                playerInv.setItem(i, leftovers.values().iterator().next());
            }
        }
    }

    // Transfers ALL items from the container into player's main inventory (slots 9-35).
    public static void transferAllToInventory(Inventory container, Player player) {
        Inventory playerInv = player.getInventory();
        for (int i = 0; i < container.getSize(); i++) {
            ItemStack item = container.getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            Map<Integer, ItemStack> leftovers = playerInv.addItem(item);
            if (leftovers.isEmpty()) {
                container.setItem(i, null);
            } else {
                container.setItem(i, leftovers.values().iterator().next());
            }
        }
    }
}
