package com.toonystank.requisite.Modules;

import com.toonystank.requisite.Modules.managers.BaseModule;
import com.toonystank.requisite.Requisite;
import com.toonystank.requisite.utils.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnderChestCommand extends BaseModule {

    private String openedEnderChestMessage = "Opened {player}'s ender chest.";

    public EnderChestCommand(Requisite plugin) {
        super(plugin, "enderchest", new Command("enderchest",
                false,
                false,
                "Open your enderchest",
                "/enderchest [player]",
                "rollerite.enderchest",
                List.of("ec")));
    }

    @Override
    public void setLanguages() throws IOException {
        openedEnderChestMessage = getLanguageConfig().getString("enderchest.opened-enderchest", openedEnderChestMessage);
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getCommandData().usage());
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(getLanguageConfig().getPlayerNotFound());
            return;
        }

        target.openInventory(target.getEnderChest());
        LanguageConfig.builder().setPlayer(target.getName()).buildAndSend(openedEnderChestMessage,sender,true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.openInventory(player.getEnderChest());
            return;
        }

        if (!player.hasPermission("rollerite.enderchest.others")) {
            LanguageConfig.builder().buildAndSend(getLanguageConfig().getNoPermission(),player,true);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            LanguageConfig.builder().setPlayer(args[0]).buildAndSend(getLanguageConfig().getPlayerNotFound(),player,true);
            return;
        }

        target.openInventory(target.getEnderChest());
        LanguageConfig.builder().setPlayer(target.getName()).buildAndSend(openedEnderChestMessage,player,true);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1 && sender.hasPermission("rollerite.enderchest.others")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        }

        return suggestions;
    }
}