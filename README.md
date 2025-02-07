# **ChestSortPlugin**

## **Plugin Overview**

- **Name:** ChestSortPlugin  
- **Version:** 1.1  
- **Description:**  
  ChestSortPlugin is a lightweight Minecraft plugin for **Paper 1.21.4** servers that automatically sorts items in chests and player inventories when shift-clicking on an **empty slot**. The sorting prioritizes **weapons**, followed by **tools**, and then **blocks alphabetically**. It respects stack limits (max 64 items per stack) and protects hotbar and worn armor from being altered.

## **Features**

1. **Shift+Click Sorting**  
   - **Function:** Automatically sorts the chest or player inventory when the player shift-clicks on an **empty slot**.
   - **Sorting Priority:** 
     1. **Weapons** (Swords, Bows, Crossbows, Tridents)
     2. **Tools** (Pickaxes, Axes, Shovels, Hoes, Shears)
     3. **Armor** (Helmets, Chestplates, Leggings, Boots, Shields)
     4. **Blocks** (Alphabetically)
   - **Rules:** 
     - Does **not** touch hotbar items.
     - Respects **64-item stack limit**.
     - Does **not** affect worn armor.

2. **/chestsort Command**  
   - **Function:** Allows players to toggle shift-click sorting **on** or **off** with `/chestsort on` or `/chestsort off`.
   - **Permissions:** All players have access to this command by default (no additional permission setup required).

## **Installation**

1. **Download the JAR file** from the [Releases](https://github.com/sooknu/ChestSortPlugin/releases) section.
2. Place the **`ChestSortPlugin-1.1.jar`** file into your server's **`/plugins`** folder.
3. Restart or reload your server.

## **Usage**

- **Shift+Click on an empty slot** in a chest or inventory to trigger sorting.
- Use `/chestsort on` to enable sorting or `/chestsort off` to disable it.

## **Compatibility**

- **Minecraft Version:** Paper 1.21.4
- **Dependencies:** None

## **Support**

For issues or suggestions, please visit the [GitHub Issues](https://github.com/sooknu/ChestSortPlugin/issues) page.

---

Enjoy a clutter-free inventory experience with **ChestSortPlugin**! 🚀

