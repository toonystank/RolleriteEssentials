package com.toonystank.requisite.utils;

import com.toonystank.requisite.Requisite;
import org.bukkit.NamespacedKey;

public class NameSpace {

    public enum KEYS{
        GuiItem("re-gui-item");

        public final String key;
        KEYS(String s) {
            this.key = s;
        }
        public NamespacedKey getKey() {
            return new NamespacedKey(Requisite.getInstance(), key);
        }
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(Requisite.getInstance(), key);
    }
}
