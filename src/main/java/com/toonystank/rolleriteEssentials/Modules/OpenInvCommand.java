package com.toonystank.rolleriteEssentials.Modules;

import com.toonystank.rolleriteEssentials.Modules.managers.BaseModule;
import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import com.toonystank.rolleriteEssentials.utils.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.List;

public class OpenInvCommand extends BaseModule {

    private String viewingInventoryMessage = "&aYou are now viewing {player}'s inventory.";


    public OpenInvCommand(RolleriteEssentials plugin) {
        super(plugin, "openinv", new Command("openinv",
                true,
                true,
                "Opens another player's inventory for editing",
                "/openinv <player>",
                "RolleriteEssentials.openinv",
                List.of("invsee"))
        );
    }

    @Override
    public void setLanguages() throws IOException {
        viewingInventoryMessage = getLanguageConfig().getString("openinv.viewingInventoryMessage", viewingInventoryMessage);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            LanguageConfig.builder().buildAndSend(getCommandData().usage(),player,true);
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            LanguageConfig.builder().buildAndSend(getLanguageConfig().getPlayerNotFound(), player,true);
            return;
        }

        Inventory targetInventory = targetPlayer.getInventory();
        player.openInventory(targetInventory);
        LanguageConfig.builder().setPlayer(player.getName()).buildAndSend(viewingInventoryMessage, player,true);
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        LanguageConfig.builder().buildAndSend(getLanguageConfig().getPlayerOnly(), sender,true);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return List.of();
    }
}
