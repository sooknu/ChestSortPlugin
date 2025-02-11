// File: ChestSortPlugin.java
package sooknu.chestsort;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for ChestSortPlugin.
 */
public class ChestSortPlugin extends JavaPlugin {

    private static ChestSortPlugin instance;
    private boolean sortingEnabled = true; // Global toggle (from config)

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.sortingEnabled = getConfig().getBoolean("sorting-enabled", true);

        // Register commands.
        this.getCommand("chestsort").setExecutor(new ChestSortCommand(this));
        this.getCommand("invsort").setExecutor(new InvSortCommand(this));

        // Register listeners.
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SettingsGUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(this), this);

        getLogger().info("ChestSortPlugin v" + getDescription().getVersion() + " enabled!");
    }

    public static ChestSortPlugin getInstance() {
        return instance;
    }

    public boolean isSortingEnabled() {
        return sortingEnabled;
    }

    public void setSortingEnabled(boolean enabled) {
        this.sortingEnabled = enabled;
    }
}
