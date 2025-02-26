package com.toonystank.requisite;

import com.toonystank.requisite.Modules.managers.BaseCommand;
import com.toonystank.requisite.Modules.managers.BaseModule;
import com.toonystank.requisite.utils.LanguageConfig;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class DefaultCommand extends BaseCommand {

    private final Map<String, BaseModule> subCommands = new HashMap<>();

    public DefaultCommand(Requisite plugin, String name) {
        super(plugin, name, false, false, "Main command for Rollerite Essentials", "/" + name + " <subcommand>", "RolleriteEssentials.use", List.of("re","essentials","rollerite"));
        subCommands.put("help",null);
    }

    public void registerSubCommand(BaseModule module) {
        subCommands.put(module.getName().toLowerCase(), module);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("&cUsage: /" + getCommandData().name() + " <subcommand>");
            return;
        }
        if (args[0].equalsIgnoreCase("help")) {
            handleHelpCommand(player, args);
            return;
        }
        BaseModule module = subCommands.get(args[0].toLowerCase());
        if (module == null) {
            player.sendMessage("&cUnknown subcommand.");
            return;
        }
        module.execute(player, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        if (args.length == 0) {
            LanguageConfig.builder().buildAndSend("Usage: /" + getCommandData().name() + " <subcommand>",sender,true);
            return;
        }
        if (args[0].equalsIgnoreCase("help")) {
            handleHelpCommand(sender, args);
            return;
        }
        BaseModule module = subCommands.get(args[0].toLowerCase());
        if (module == null) {
            LanguageConfig.builder().buildAndSend("Unknown subcommand.",sender,true);
            return;
        }
        module.execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getMatchingSubCommands(args[0]);
        }
        if (args[0].equalsIgnoreCase("help") && args.length == 2) {
            return getMatchingSubCommands(args[1]);
        }
        BaseModule module = subCommands.get(args[0].toLowerCase());
        return (module != null) ? module.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length)) : Collections.emptyList();
    }

    private List<String> getMatchingSubCommands(String input) {
        List<String> matches = new ArrayList<>();
        for (String cmd : subCommands.keySet()) {
            if (cmd.startsWith(input.toLowerCase())) {
                matches.add(cmd);
            }
        }
        matches.sort(String::compareTo);
        return matches;
    }

    private void handleHelpCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            LanguageConfig.builder().buildAndSend("&eAvailable Commands:",sender,true);
            for (Map.Entry<String, BaseModule> entry : subCommands.entrySet()) {
                if (entry == null || entry.getKey() == null || entry.getValue() == null) continue;
                LanguageConfig.builder().buildAndSend("&6/" + getCommandData().name() + " " + entry.getKey() + " - &f" + entry.getValue().getCommandData().description(),sender,false);
            }
            return;
        }
        BaseModule module = subCommands.get(args[1].toLowerCase());
        if (module == null) {
            LanguageConfig.builder().buildAndSend("&cUnknown subcommand.",sender,true);
            return;
        }
        LanguageConfig.builder().buildAndSend("&eHelp for /" + getCommandData().name() + " " + args[1] + ":",sender,true);
        LanguageConfig.builder().buildAndSend("&6Description: &f" + module.getCommandData().description(),sender,false);
        LanguageConfig.builder().buildAndSend("&6Usage: &f" + module.getCommandData().usage(),sender,false);
    }
}
