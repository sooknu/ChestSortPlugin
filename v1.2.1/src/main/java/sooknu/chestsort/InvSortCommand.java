// File: InvSortCommand.java
package sooknu.chestsort;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

/**
 * Command executor for /invsort (or /isort) to manually sort the player's inventory.
 */
public class InvSortCommand implements CommandExecutor {

    private final ChestSortPlugin plugin;

    public InvSortCommand(ChestSortPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.getConfig().getBoolean("use-permissions", true) && !sender.hasPermission("chestsort.use.inventory")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use inventory sorting.");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used in-game.");
            return true;
        }
        Player player = (Player) sender;
        // Manual sort: only sort main inventory (hotbar remains untouched).
        InventorySorter.sortPlayerInventory(player);
        player.sendMessage(ChatColor.GREEN + "Your inventory has been sorted.");
        return true;
    }
}
