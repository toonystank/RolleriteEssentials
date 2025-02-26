package com.toonystank.requisite.Modules;

import com.toonystank.requisite.Modules.managers.BaseModule;
import com.toonystank.requisite.Requisite;
import com.toonystank.requisite.utils.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameModeCommand extends BaseModule {

    private String successOtherMessage;
    private String successSelfMessage;
    private String invalidGamemodeMessage;
    private String consoleUsageMessage;

    public GameModeCommand(Requisite plugin) {
        super(plugin, "gamemode", new Command("gamemode",
                false,
                true,
                "Change the player's gamemode to survival, creative, adventure, or spectator",
                "/gamemode <gamemode> [player]",
                "RolleriteEssentials.gamemode",
                List.of("gm"))
        );
    }

    @Override
    public void setLanguages() throws IOException {
        successOtherMessage = super.getLanguageConfig().getString("gamemode.success", "&aSuccessfully changed {player}'s gamemode to {parma}");
        successSelfMessage = super.getLanguageConfig().getString("gamemode.success.self", "&aSuccessfully changed your gamemode to {parma}");
        invalidGamemodeMessage = super.getLanguageConfig().getString("gamemode.invalid", "&cInvalid gamemode. Use: survival, creative, adventure, spectator.");
        consoleUsageMessage = super.getLanguageConfig().getString("gamemode.console_usage", "&cConsole must specify a player: /gamemode <gamemode> <player>");
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        if (args.length < 2) {
            LanguageConfig.builder().buildAndSend(consoleUsageMessage,sender,true);
            return;
        }

        GameMode gameMode = getGameMode(args[0]);
        if (gameMode == null) {
            LanguageConfig.builder().buildAndSend(invalidGamemodeMessage,sender,true);
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            sender.sendMessage(this.getLanguageConfig().getPlayerNotFound());
            return;
        }

        targetPlayer.setGameMode(gameMode);
        LanguageConfig.builder().setPlayer(targetPlayer.getName()).setParma(gameMode.name()).buildAndSend(successOtherMessage,sender,true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            LanguageConfig.builder().buildAndSend(invalidGamemodeMessage,player,true);
            return;
        }

        GameMode gameMode = getGameMode(args[0]);
        if (gameMode == null) {
            LanguageConfig.builder().buildAndSend(invalidGamemodeMessage,player,true);
            return;
        }

        if (args.length == 1) {
            player.setGameMode(gameMode);
            LanguageConfig.builder().setParma(gameMode.name()).buildAndSend(successSelfMessage,player,true);
        } else {
            if (!player.hasPermission("RolleriteEssentials.gamemode.others")) {
                LanguageConfig.builder().buildAndSend(this.getLanguageConfig().getNoPermission(),player,true);
                return;
            }

            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                player.sendMessage(this.getLanguageConfig().getPlayerNotFound());
                return;
            }

            targetPlayer.setGameMode(gameMode);
            LanguageConfig.builder().setPlayer(targetPlayer.getName()).setParma(gameMode.name()).buildAndSend(successOtherMessage,player,true);
            LanguageConfig.builder().setPlayer(targetPlayer.getName()).setParma(gameMode.name()).buildAndSend(successSelfMessage,targetPlayer,true);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            for (GameMode gm : GameMode.values()) {
                suggestions.add(gm.name().toLowerCase());
            }
        } else if (args.length == 2 && sender.hasPermission("RolleriteEssentials.gamemode.others")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        }

        return suggestions;
    }

    private GameMode getGameMode(String input) {
        try {
            return GameMode.valueOf(input.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
