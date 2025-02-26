package com.toonystank.rolleriteEssentials.gui;

import com.toonystank.rolleriteEssentials.utils.NameSpace;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Represents an item used in a GUI with optional actions when clicked.
 */
@Getter
@SuppressWarnings("unused")
public class GuiItem {
    private final UUID uuid = UUID.randomUUID();
    private GuiAction<InventoryClickEvent> action;
    private ItemStack itemStack;

    /**
     * Creates a GuiItem with a specified ItemStack and action.
     *
     * @param itemStack The item stack to use.
     * @param action The action to execute on click.
     */
    public GuiItem(final ItemStack itemStack, final GuiAction<InventoryClickEvent> action) {
        this.action = action;
        setItemStack(itemStack);
    }

    public GuiItem(final ItemStack itemStack) {
        this(itemStack, null);
    }

    public GuiItem(final Material material) {
        this(new ItemStack(material), null);
    }

    public GuiItem(final Material material, final GuiAction<InventoryClickEvent> action) {
        this(new ItemStack(material), action);
    }

    /**
     * Sets the item stack while applying a unique identifier to its metadata.
     *
     * @param itemStack The item stack to set.
     */
    public void setItemStack(final ItemStack itemStack) {
        this.itemStack = new ItemBuilder(itemStack.getType())
                .setCustomData(NameSpace.KEYS.GuiItem.getKey(), uuid.toString())
                .build();
    }

    /**
     * Sets a new action for the GUI item.
     *
     * @param action The action to set.
     */
    public void setAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        this.action = action;
    }

    /**
     * Builder class for creating customized GUI items.
     */
    public static class ItemBuilder {
        private final ItemStack itemStack;
        private final ItemMeta itemMeta;

        public ItemBuilder(Material material) {
            this.itemStack = new ItemStack(material);
            this.itemMeta = itemStack.getItemMeta();
        }

        public ItemBuilder name(Component name) {
            if (itemMeta != null) {
                itemMeta.displayName(name);
            }
            return this;
        }

        public ItemBuilder lore(List<Component> lore) {
            if (itemMeta != null) {
                itemMeta.lore(lore);
            }
            return this;
        }

        public ItemBuilder amount(int amount) {
            itemStack.setAmount(amount);
            return this;
        }

        public ItemBuilder model(int model) {
            if (itemMeta != null) {
                itemMeta.setCustomModelData(model);
            }
            return this;
        }

        public ItemBuilder setCustomData(NamespacedKey key, String value) {
            if (itemMeta != null) {
                itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
            }
            return this;
        }

        public ItemStack build() {
            if (itemMeta != null) {
                itemStack.setItemMeta(itemMeta);
            }
            return itemStack;
        }
    }
}
