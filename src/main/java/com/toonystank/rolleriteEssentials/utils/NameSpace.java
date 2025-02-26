package com.toonystank.rolleriteEssentials.utils;

import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import org.bukkit.NamespacedKey;

public class NameSpace {

    public enum KEYS{
        GuiItem("re-gui-item");

        public final String key;
        KEYS(String s) {
            this.key = s;
        }
        public NamespacedKey getKey() {
            return new NamespacedKey(RolleriteEssentials.getInstance(), key);
        }
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(RolleriteEssentials.getInstance(), key);
    }
}
