// File: ItemStackComparator.java
package sooknu.chestsort;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.Comparator;

public class ItemStackComparator implements Comparator<ItemStack> {

    @Override
    public int compare(ItemStack a, ItemStack b) {
        int catA = getCategory(a);
        int catB = getCategory(b);
        if (catA != catB) {
            return catA - catB;
        }
        // If in same category, sort alphabetically by material name.
        return a.getType().name().compareTo(b.getType().name());
    }
    
    private int getCategory(ItemStack item) {
        Material m = item.getType();
        String name = m.name();
        // Category 1: Weapons
        if (name.contains("SWORD") || name.equals("BOW") || name.equals("CROSSBOW") || name.contains("TRIDENT")) {
            return 1;
        }
        // Category 2: Tools
        if (name.contains("PICKAXE") || name.contains("AXE") || name.contains("SHOVEL") || name.contains("HOE")
                || name.equals("SHEARS") || name.equals("FLINT_AND_STEEL")) {
            return 2;
        }
        // Category 3: Armor
        if (name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS")
                || name.equals("SHIELD")) {
            return 3;
        }
        // Category 5: Blocks (including containers)
        if (m.isBlock()) {
            return 5;
        }
        // Category 4: Other items
        return 4;
    }
}
