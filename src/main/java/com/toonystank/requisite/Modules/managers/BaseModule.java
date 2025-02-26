package com.toonystank.requisite.Modules.managers;

import com.toonystank.requisite.Requisite;
import com.toonystank.requisite.utils.LanguageConfig;
import com.toonystank.requisite.utils.MessageUtils;
import lombok.Getter;

import java.io.IOException;

@Getter
public abstract class BaseModule extends BaseCommand {

    private final String name;
    private LanguageConfig languageConfig;

    public BaseModule(Requisite plugin, String name, BaseCommand.Command command) {
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
