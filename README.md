## Plugin Overview

- **Name:** ChestSortPlugin
- **Version:** 1.2  
- **Description:**  
  ChestSortPlugin is a lightweight Minecraft plugin for **Paper 1.21.4** servers that automatically sorts items in chests and player inventories using multiple configurable methods. It sorts items based on a configurable category order (e.g., Weapons, Tools, Armor, Others, Blocks) while preserving item metadata, enchantments, and other properties. The plugin ensures that hotbar items and worn armor remain unaffected and provides fine-grained control over which container types can be sorted.

---

## Features

1. **Shift+Click Sorting**
    - **Function:** Automatically sorts the chest or player inventory when a player shift-clicks on an empty slot.
    - **Sorting Priority:**  
      The order is configurable in the `config.yml` under `sorting_order`. By default, it might be:
      1. **WEAPONS** (Swords, Bows, Crossbows, Tridents)  
      2. **TOOLS** (Pickaxes, Axes, Shovels, Hoes, Shears)  
      3. **ARMOR** (Helmets, Chestplates, Leggings, Boots, Shields)  
      4. **OTHERS** (Miscellaneous items)  
      5. **BLOCKS** (Sorted alphabetically)
      
      - Does **not** modify hotbar items.
      - Respects stack limits (max 64 items per stack).
      - Does **not** alter worn armor.
      
2. **Auto Chest Sorting**
    - **Function:** When enabled, automatically sorts the contents of chests (or other containers) upon closing (except for container types you exclude in config).

3. **Per-Container Toggling**
    - **Function:** In the `config.yml`, you can now **enable or disable sorting** on a per-container basis.  
      For instance, you can set `"HOPPER: false"` to skip sorting hoppers, or `"BREWING: false"` to skip sorting brewing stands, while keeping `"CHEST: true"` and `"BARREL: true"`.
    - **Note:** This feature ensures that only container types explicitly enabled are sorted, providing complete control over the plugin’s behavior.

4. **Additional Hotkeys**
    - **Function:** Provides hotkey actions for transferring items between a chest and the player’s inventory:
      - **Left-Click Outside:** Transfers matching items (except hotbar) **to chest**.
      - **Right-Click Outside:** Transfers matching items **from chest** to inventory.
      - **Double-Left-Click Outside:** Transfers **all items** (except hotbar) **to chest**.
      - **Double-Right-Click Outside:** Transfers **all items** **from chest** into inventory.
    - **Note:** These actions are enabled or disabled via the in-game settings GUI.
    
5. **Unified Settings GUI**
    - **Function:** Use the `/chestsort` command (with no arguments) to open a settings GUI where players can toggle key features on or off.

6. **Configurable Options**
    - **Sorting Order:**  
      Define the category order for sorting in the `config.yml` under `sorting_order`.
    - **Per-Container Sorting:**  
      Under the `sorting_enabled` section in `config.yml`, specify which container types can be sorted (`true`) or skipped (`false`).
    - **Messages:**  
      All in-game messages (e.g., sort completion, transfer notifications, usage reminders) are configurable and can be disabled entirely.
    - **Double-Click Delay:**  
      The delay for detecting double-clicks for hotkey functions is configurable.
    - **Sound Settings:**  
      Configure the sorting sound and the GUI toggle sound.

---

## Installation

1. **Download the JAR file** from the [Releases](https://github.com/sooknu/ChestSortPlugin/releases) section.
2. Place the **`ChestSortPlugin-1.2.jar`** file into your server's **`plugins`** folder.
3. Restart or reload your server.
4. (Optional) Customize settings in the generated `config.yml`.

---

## Usage

- **Shift-Click Sorting:**  
  Shift-click an empty slot in a chest or in your inventory to sort items.

- **Auto Chest Sorting:**  
  When enabled, chests are automatically sorted upon closing (for container types set to `true` in the config).

- **Per-Container Toggling:**
  Configure individual container types (e.g., CHEST, HOPPER, BREWING) in your `config.yml` under `sorting_enabled`. Any container type set to `false` is ignored during sorting.

- **Additional Hotkeys:**
  - **Left-Click Outside:** Transfers matching items (except hotbar) **to chest**.
  - **Right-Click Outside:** Transfers matching items **from chest** to inventory.
  - **Double-Left-Click Outside:** Transfers **all items** (except hotbar) **to chest**.
  - **Double-Right-Click Outside:** Transfers **all items** **from chest** into inventory.

- **Settings GUI:**  
  Use the `/chestsort` command (with no arguments) to open the unified settings GUI where you can toggle these features.

- **Configuration:**  
  All settings—including sorting order, message options, container toggles, and double-click delay—are configurable via `config.yml`.

---

## Compatibility

- **Minecraft Version:** Paper 1.21.4
- **Dependencies:** None

---

## Support

For issues or suggestions, please visit the [GitHub Issues](https://github.com/sooknu/ChestSortPlugin/issues) page.

---

Enjoy a clutter-free inventory experience with **ChestSortPlugin**! 🚀
