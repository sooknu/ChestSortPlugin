// File: PlayerSettings.java
package sooknu.chestsort;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class PlayerSettings {
    private static final Map<UUID, PlayerSettings> settings = new HashMap<>();

    // Core settings
    private boolean shiftClickSortEnabled = true;       // Enabled by default
    private boolean autoContainerSortEnabled = false;     // Disabled by default

    // Additional Hotkey toggles (all disabled by default)
    private boolean leftClickOutsideToContainer = false;  // Transfers matching items (except hotbar) to chest
    private boolean rightClickOutsideToInventory = false; // Transfers matching items from chest to inventory
    private boolean doubleLeftClickOutside = false;       // Transfers all items (except hotbar) to chest
    private boolean doubleRightClickOutside = false;      // Transfers all items from chest into inventory

    public static PlayerSettings get(UUID uuid) {
        return settings.computeIfAbsent(uuid, k -> new PlayerSettings());
    }

    // Core settings getters and setters.
    public boolean isShiftClickSortEnabled() {
        return shiftClickSortEnabled;
    }
    public void setShiftClickSortEnabled(boolean enabled) {
        this.shiftClickSortEnabled = enabled;
    }
    public boolean isAutoContainerSortEnabled() {
        return autoContainerSortEnabled;
    }
    public void setAutoContainerSortEnabled(boolean enabled) {
        this.autoContainerSortEnabled = enabled;
    }

    // Additional Hotkey toggles getters and setters.
    public boolean isLeftClickOutsideToContainer() {
        return leftClickOutsideToContainer;
    }
    public void setLeftClickOutsideToContainer(boolean enabled) {
        this.leftClickOutsideToContainer = enabled;
    }
    public boolean isRightClickOutsideToInventory() {
        return rightClickOutsideToInventory;
    }
    public void setRightClickOutsideToInventory(boolean enabled) {
        this.rightClickOutsideToInventory = enabled;
    }
    public boolean isDoubleLeftClickOutside() {
        return doubleLeftClickOutside;
    }
    public void setDoubleLeftClickOutside(boolean enabled) {
        this.doubleLeftClickOutside = enabled;
    }
    public boolean isDoubleRightClickOutside() {
        return doubleRightClickOutside;
    }
    public void setDoubleRightClickOutside(boolean enabled) {
        this.doubleRightClickOutside = enabled;
    }
}
