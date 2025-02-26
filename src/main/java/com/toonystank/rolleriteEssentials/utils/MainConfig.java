package com.toonystank.rolleriteEssentials.utils;

import lombok.Getter;

import java.io.IOException;

@Getter
public class MainConfig extends FileConfig {

    private boolean smallText;
    private boolean debug;
    private LanguageConfig languageConfig;


    public MainConfig() throws IOException {
        super("config.yml",false,true);
        load();
    }

    public void load() throws IOException {
        smallText = getBoolean("smallText",true);
        debug = getBoolean("debug",false);
        if (languageConfig == null) {
            languageConfig = new LanguageConfig();
        }else {
            languageConfig.reload();
        }
    }

    @Override
    public void reload() throws IOException {
        super.reload();
        load();
    }
}
