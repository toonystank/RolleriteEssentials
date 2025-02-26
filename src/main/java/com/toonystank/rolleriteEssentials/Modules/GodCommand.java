package com.toonystank.rolleriteEssentials.Modules;

import com.toonystank.rolleriteEssentials.Modules.managers.BaseModule;
import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import com.toonystank.rolleriteEssentials.utils.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GodCommand extends BaseModule {

    private String godEnabledSelf;
    private String godDisabledSelf;
    private String godEnabledOther;
    private String godDisabledOther;
    private String noPermissionMessage;

    public GodCommand(RolleriteEssentials plugin) {
        super(plugin, "god", new Command("god",
                false,
                false,
                "Toggle god mode (invulnerability)",
                "/god [player]",
                "RolleriteEssentials.god",
                List.of())
        );
    }

    @Override
    public void setLanguages() throws IOException {
        godEnabledSelf = super.getLanguageConfig().getString("god.enabled.self", "&aYou are now in god mode.");
        godDisabledSelf = super.getLanguageConfig().getString("god.disabled.self", "&cYou are no longer in god mode.");
        godEnabledOther = super.getLanguageConfig().getString("god.enabled.other", "&aEnabled god mode for {player}.");
        godDisabledOther = super.getLanguageConfig().getString("god.disabled.other", "&cDisabled god mode for {player}.");
        noPermissionMessage = super.getLanguageConfig().getString("god.no_permission", "&cYou don't have permission to do that.");
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        if (args.length == 0) {
            LanguageConfig.builder().buildAndSend(getCommandData().usage(),sender,true);
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            LanguageConfig.builder().buildAndSend(getLanguageConfig().getPlayerNotFound(),sender,true);
            return;
        }

        toggleGodMode(sender, target);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            toggleGodMode(player, player);
            return;
        }

        if (!player.hasPermission("RolleriteEssentials.god.others")) {
            LanguageConfig.builder().buildAndSend(noPermissionMessage,player,true);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            LanguageConfig.builder().buildAndSend(getLanguageConfig().getPlayerNotFound(),player,true);
            return;
        }

        toggleGodMode(player, target);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1 && sender.hasPermission("RolleriteEssentials.god.others")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        }

        return suggestions;
    }

    private void toggleGodMode(CommandSender sender, Player target) {
        boolean isGodModeEnabled = target.isInvulnerable();
        target.setInvulnerable(!isGodModeEnabled);

        if (target.equals(sender)) {
            LanguageConfig.builder().setPlayer(target.getName()).buildAndSend(isGodModeEnabled ? godDisabledSelf : godEnabledSelf,sender,true);
        } else {
            LanguageConfig.builder().setPlayer(target.getName()).buildAndSend(isGodModeEnabled ? godDisabledOther : godEnabledOther,sender,true);
            LanguageConfig.builder().setPlayer(target.getName()).buildAndSend(isGodModeEnabled ? godDisabledSelf : godEnabledSelf,target,true);}
    }
}
