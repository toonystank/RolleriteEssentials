package com.toonystank.requisite.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;

public class PlayerGUI extends BaseGui{

    public PlayerGUI(Component title) {
        super(InventoryType.PLAYER,title);

    }
}
