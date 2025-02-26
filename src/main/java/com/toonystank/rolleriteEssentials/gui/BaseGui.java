package com.toonystank.rolleriteEssentials.gui;

import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@SuppressWarnings("unused")
public abstract class BaseGui implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, GuiItem> guiItems;
    @Setter
    private GuiAction<InventoryCloseEvent> closeAction;

    private GuiListener listener;

    protected BaseGui() {
        this.inventory = Bukkit.createInventory(this, InventoryType.PLAYER);
        this.guiItems = new LinkedHashMap<>(inventory.getSize());
        setGuiListener();
    }

    protected BaseGui(int size) {
        this.inventory = Bukkit.createInventory(this, size);
        this.guiItems = new LinkedHashMap<>(size);
        setGuiListener();
    }

    protected BaseGui(InventoryType type) {
        this.inventory = Bukkit.createInventory(this, type);
        this.guiItems = new LinkedHashMap<>(inventory.getSize());
        setGuiListener();
    }
    protected BaseGui(InventoryType type, Component title) {
        this.inventory = Bukkit.createInventory(this, type, title);
        this.guiItems = new LinkedHashMap<>(inventory.getSize());
        setGuiListener();
    }

    private void setGuiListener() {
        RolleriteEssentials plugin = RolleriteEssentials.getInstance();
        this.listener = new GuiListener(this);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Sets an item in the specified slot after validating the slot.
     *
     * @param slot The slot to place the item in.
     * @param item The item to add.
     */
    public void setItem(int slot, GuiItem item) {
        if (slot < 0 || slot >= inventory.getSize()) {
            throw new IllegalArgumentException("Invalid slot index: " + slot);
        }
        guiItems.put(slot, item);
        inventory.setItem(slot, item.getItemStack());
    }

    /**
     * Removes an item from the specified slot after validating the slot.
     *
     * @param slot The slot to remove the item from.
     */
    public void removeItem(int slot) {
        if (slot < 0 || slot >= inventory.getSize()) {
            throw new IllegalArgumentException("Invalid slot index: " + slot);
        }
        if (guiItems.containsKey(slot)) {
            guiItems.remove(slot);
            inventory.setItem(slot, null);
        }
    }

    /**
     * Removes the first occurrence of a given item from the GUI.
     *
     * @param item The item to remove.
     * @return True if the item was removed, false if it was not found.
     */
    public boolean removeItem(GuiItem item) {
        Iterator<Map.Entry<Integer, GuiItem>> iterator = guiItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, GuiItem> entry = iterator.next();
            if (entry.getValue().equals(item)) {
                iterator.remove();
                inventory.setItem(entry.getKey(), null);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds an item to the GUI in the first available empty slot.
     *
     * @param item The item to add.
     * @return True if the item was added successfully, false if the inventory is full.
     */
    public boolean addItem(GuiItem item) {
        int slot = inventory.firstEmpty();
        if (slot == -1) {
            return false;
        }
        setItem(slot, item);
        return true;
    }

    /**
     * Opens the GUI for a player, ensuring items are placed correctly.
     *
     * @param player The player to open the GUI for.
     */
    public void open(Player player) {
        guiItems.forEach((slot, item) -> inventory.setItem(slot, item.getItemStack()));
        player.openInventory(inventory);
    }
}
