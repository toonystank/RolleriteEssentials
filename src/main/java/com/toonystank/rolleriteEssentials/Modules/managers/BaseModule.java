package com.toonystank.rolleriteEssentials.Modules.managers;

import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import com.toonystank.rolleriteEssentials.utils.LanguageConfig;
import com.toonystank.rolleriteEssentials.utils.MessageUtils;
import lombok.Getter;

import java.io.IOException;

@Getter
public abstract class BaseModule extends BaseCommand {

    private final String name;
    private LanguageConfig languageConfig;

    public BaseModule(RolleriteEssentials plugin, String name, BaseCommand.Command command) {
        super(plugin,command);
        this.name = name;
        this.languageConfig = plugin.getMainConfig().getLanguageConfig();
        try {
            setLanguages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MessageUtils.toConsole("Loaded " + name + " module",false);
    }
    public abstract void setLanguages() throws IOException;

    public void reload() {
        this.languageConfig = getPlugin().getMainConfig().getLanguageConfig();
    }



}
