package com.toonystank.rolleriteEssentials.Modules;

import com.toonystank.rolleriteEssentials.Modules.managers.BaseModule;
import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import com.toonystank.rolleriteEssentials.utils.LanguageConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class ReloadCommand extends BaseModule {

    private String reloadMessage = "Plugin reloaded successfully.";


    public ReloadCommand(RolleriteEssentials plugin) {
        super(plugin, "rolleritereload", new Command("rolleritereload",
                false,
                false,
                "Reloads the plugin",
                "/rolleritereload",
                "RolleriteEssentials.reload",
                List.of("rer")));
    }

    @Override
    public void setLanguages() throws IOException {
        reloadMessage = getLanguageConfig().getString("reload.reloadMessage", reloadMessage);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        try {
            getPlugin().reloadPlugin();
            LanguageConfig.builder().buildAndSend(reloadMessage, sender, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(Player player, String[] args) {

    }
}
