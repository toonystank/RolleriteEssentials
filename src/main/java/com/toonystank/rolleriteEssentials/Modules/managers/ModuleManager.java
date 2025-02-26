package com.toonystank.rolleriteEssentials.Modules.managers;

import com.toonystank.rolleriteEssentials.DefaultCommand;
import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ModuleManager {

    private final Map<String, BaseModule> modules = new HashMap<>();
    private final DefaultCommand defaultCommand;

    public ModuleManager(RolleriteEssentials plugin) {
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
