package com.toonystank.rolleriteEssentials.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;


public class GuiListener implements Listener {

    private final BaseGui baseGui;

    public GuiListener(BaseGui baseGui) {
        this.baseGui = baseGui;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof BaseGui gui) {
            gui.getCloseAction().execute(event,baseGui);

        }
    }

}
