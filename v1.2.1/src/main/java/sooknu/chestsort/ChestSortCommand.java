// File: ChestSortCommand.java
package sooknu.chestsort;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class ChestSortCommand implements CommandExecutor {

    private final ChestSortPlugin plugin;

    public ChestSortCommand(ChestSortPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.getConfig().getBoolean("use-permissions", true) && !sender.hasPermission("chestsort.use")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use chest sorting.");
            return true;
        }
        if (args.length == 0) {
            if (sender instanceof Player) {
                SettingsGUI.open((Player) sender, plugin);
            } else {
                sender.sendMessage("Usage: /chestsort reload");
            }
            return true;
        }
        String arg = args[0].toLowerCase();
        switch (arg) {
            case "on":
                PlayerSettings.get(((Player) sender).getUniqueId()).setAutoContainerSortEnabled(true);
                sender.sendMessage(ChatColor.GREEN + "Auto Chest Sorting enabled.");
                break;
            case "off":
                PlayerSettings.get(((Player) sender).getUniqueId()).setAutoContainerSortEnabled(false);
                sender.sendMessage(ChatColor.RED + "Auto Chest Sorting disabled.");
                break;
            case "toggle":
                PlayerSettings ps = PlayerSettings.get(((Player) sender).getUniqueId());
                ps.setAutoContainerSortEnabled(!ps.isAutoContainerSortEnabled());
                sender.sendMessage(ChatColor.YELLOW + "Auto Chest Sorting toggled to " +
                        (ps.isAutoContainerSortEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
                break;
            case "reload":
                if (plugin.getConfig().getBoolean("use-permissions", true) && !sender.hasPermission("chestsort.reload")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to reload the config.");
                    return true;
                }
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "ChestSort config reloaded.");
                break;
            case "help":
                sender.sendMessage(ChatColor.AQUA + "ChestSort Commands:");
                sender.sendMessage(ChatColor.YELLOW + "/chestsort on|off|toggle" + ChatColor.WHITE + " - Toggle auto chest sorting.");
                sender.sendMessage(ChatColor.YELLOW + "/chestsort reload" + ChatColor.WHITE + " - Reload config.");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /chestsort help for usage.");
                break;
        }
        return true;
    }
}
