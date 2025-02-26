package com.toonystank.requisite;

import com.toonystank.requisite.Modules.*;
import com.toonystank.requisite.Modules.managers.ModuleManager;
import com.toonystank.requisite.utils.MainConfig;
import com.toonystank.requisite.utils.MessageUtils;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@Getter
public final class Requisite extends JavaPlugin {

    @Getter
    private static Requisite instance;
    private MainConfig mainConfig;
    private ModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        setMainConfig();
        moduleManager = new ModuleManager(this);
        setModules();
    }
    public void setMainConfig() {
        try {
            mainConfig = new MainConfig();
        } catch (IOException e) {
            MessageUtils.error("Failed to load main configuration file." + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setModules() {
        moduleManager.registerModule(new EnderChestCommand(this));
        moduleManager.registerModule(new FixCommand(this));
        moduleManager.registerModule(new GameModeCommand(this));
        moduleManager.registerModule(new GodCommand(this));
        moduleManager.registerModule(new OpenInvCommand(this));
        moduleManager.registerModule(new TpaCommand(this));
        moduleManager.registerModule(new TrashCommand(this));
        moduleManager.registerModule(new ReloadCommand(this));
    }

    @Override
    public void onDisable() {
    }

    public void reloadPlugin() throws IOException {
        mainConfig.reload();
        moduleManager.reloadModules();
    }
}
