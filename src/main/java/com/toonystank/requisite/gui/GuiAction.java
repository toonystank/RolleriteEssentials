package com.toonystank.requisite.gui;

import org.bukkit.event.Event;

@FunctionalInterface
public interface GuiAction<T extends Event> {

    /**
     * Executes the event passed to it
     *
     * @param event   Inventory action
     * @param baseGui The base gui
     */
    void execute(final T event,BaseGui baseGui);

}

