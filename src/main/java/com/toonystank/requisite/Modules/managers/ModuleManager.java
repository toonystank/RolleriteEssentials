package com.toonystank.requisite.Modules.managers;

import com.toonystank.requisite.DefaultCommand;
import com.toonystank.requisite.Requisite;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ModuleManager {

    private final Map<String, BaseModule> modules = new HashMap<>();
    private final DefaultCommand defaultCommand;

    public ModuleManager(Requisite plugin) {
        this.defaultCommand = new DefaultCommand(plugin,"RolleriteEssentials");
    }

    public void registerModule(BaseModule module) {
        modules.put(module.getName(), module);
        defaultCommand.registerSubCommand(module);
    }
    public void registerModule(BaseModule[] modules) {
        for (BaseModule module : modules) {
            registerModule(module);
        }
    }
    public void reloadModules() {
        modules.values().forEach(BaseModule::reload);
    }

}
