package com.toonystank.rolleriteEssentials.Modules.managers;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ModuleManager {

    private final Map<String, BaseModule> modules = new HashMap<>();

    public void registerModule(BaseModule module) {
        modules.put(module.getName(), module);
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
