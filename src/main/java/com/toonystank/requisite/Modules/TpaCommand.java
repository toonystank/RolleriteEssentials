package com.toonystank.requisite.Modules;

import com.toonystank.requisite.Modules.managers.BaseModule;
import com.toonystank.requisite.Requisite;
import com.toonystank.requisite.utils.LanguageConfig;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

@Getter
public class TpaCommand extends BaseModule {

    private String requestSentMessage;
    private String requestReceivedMessage;
    private String selfRequestMessage;
    private String requestAcceptedMessage;
    private String requestDeniedMessage;
    private String noPendingRequestMessage;

    private final Map<Player, List<Player>> teleportRequests = new HashMap<>();

    public TpaCommand(Requisite plugin) {
        super(plugin, "tpa", new Command("tpa",
                false,
                true,
                "Request to teleport to another player",
                "/tpa <player>",
                "RolleriteEssentials.tpa",
                List.of("teleportask"))
        );
        BaseModule[] modules = {new TpAcceptCommand(plugin, this), new TpDenyCommand(plugin, this)};
        plugin.getModuleManager().registerModule(modules);
    }

    @Override
    public void setLanguages() throws IOException {
        requestSentMessage = super.getPlugin().getMainConfig().getLanguageConfig().getString("tpa.request_sent", "&aTeleport request sent to {player}.");
        requestReceivedMessage = super.getPlugin().getMainConfig().getLanguageConfig().getString("tpa.request_received", "&a{player} wants to teleport to you. Type /tpaccept <player> to accept or /tpdeny <player> to deny.");
        selfRequestMessage = super.getPlugin().getMainConfig().getLanguageConfig().getString("tpa.self_request", "&cYou cannot send a teleport request to yourself.");
        requestAcceptedMessage = super.getPlugin().getMainConfig().getLanguageConfig().getString("tpa.request_accepted", "&aTeleport request accepted.");
        requestDeniedMessage = super.getPlugin().getMainConfig().getLanguageConfig().getString("tpa.request_denied", "&cTeleport request denied.");
        noPendingRequestMessage = super.getPlugin().getMainConfig().getLanguageConfig().getString("tpa.no_pending_request", "&cNo pending teleport requests.");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            LanguageConfig.builder().buildAndSend("&cUsage: /tpa <player>",player,true);
            return;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            LanguageConfig.builder().buildAndSend(getPlugin().getMainConfig().getLanguageConfig().getPlayerNotFound(),player,true);
            return;
        }

        if (player.equals(targetPlayer)) {
            LanguageConfig.builder().buildAndSend(selfRequestMessage,player,true);
            return;
        }

        teleportRequests.computeIfAbsent(targetPlayer, k -> new ArrayList<>()).add(player);
        LanguageConfig.builder().setPlayer(targetPlayer.getName()).buildAndSend(requestSentMessage,player,true);
        LanguageConfig.builder().setPlayer(player.getName()).buildAndSend(requestReceivedMessage,targetPlayer,true);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : sender.getServer().getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        }
        return suggestions;
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        LanguageConfig.builder().buildAndSend(getPlugin().getMainConfig().getLanguageConfig().getPlayerOnly(),sender,true);
    }

    public boolean acceptTeleport(Player targetPlayer, Player requester) {
        List<Player> requesters = teleportRequests.get(targetPlayer);
        if (requesters == null || !requesters.remove(requester)) {
            LanguageConfig.builder().buildAndSend(noPendingRequestMessage,targetPlayer,true);
            return false;
        }
        requester.teleport(targetPlayer);
        LanguageConfig.builder().setPlayer(targetPlayer.getName()).buildAndSend(requestAcceptedMessage,requester,true);
        LanguageConfig.builder().setPlayer(requester.getName()).buildAndSend(requestAcceptedMessage,targetPlayer,true);
        return true;
    }

    public boolean denyTeleport(Player targetPlayer, Player requester) {
        List<Player> requesters = teleportRequests.get(targetPlayer);
        if (requesters == null || !requesters.remove(requester)) {
            LanguageConfig.builder().buildAndSend(noPendingRequestMessage,targetPlayer,true);
            return false;
        }
        LanguageConfig.builder().setPlayer(targetPlayer.getName()).buildAndSend(requestDeniedMessage,requester,true);
        LanguageConfig.builder().setPlayer(requester.getName()).buildAndSend(requestDeniedMessage,targetPlayer,true);
        return true;
    }
}

class TpAcceptCommand extends BaseModule {

    protected TpAcceptCommand(Requisite plugin, TpaCommand tpaCommand) {
        super(plugin, "tpaccept", new Command("tpaccept",
                false,
                false,
                "Accept a teleport request",
                "/tpaccept <player>",
                "RolleriteEssentials.tpaccept",
                List.of("tpconfirm"))
        );
        this.tpaCommand = tpaCommand;
    }

    private final TpaCommand tpaCommand;

    @Override
    public void setLanguages() {}

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            LanguageConfig.builder().buildAndSend(getCommandData().usage(), player,true);
            return;
        }
        Player requester = player.getServer().getPlayer(args[0]);
        if (requester != null) {
            tpaCommand.acceptTeleport(player, requester);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (sender instanceof Player player && args.length == 1) {
            return tpaCommand.getTeleportRequests().getOrDefault(player, Collections.emptyList())
                    .stream().map(Player::getName).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        LanguageConfig.builder().buildAndSend(getPlugin().getMainConfig().getLanguageConfig().getPlayerOnly(),sender,true);
    }
}

class TpDenyCommand extends BaseModule {

    protected TpDenyCommand(Requisite plugin, TpaCommand tpaCommand) {
        super(plugin, "tpdeny", new Command("tpdeny",
                false,
                false,
                "Deny a teleport request",
                "/tpdeny <player>",
                "RolleriteEssentials.tpdeny",
                List.of("tpcancel"))
        );
        this.tpaCommand = tpaCommand;
    }

    private final TpaCommand tpaCommand;

    @Override
    public void setLanguages() {}

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            LanguageConfig.builder().buildAndSend(getCommandData().usage(), player,true);
            return;
        }
        Player requester = player.getServer().getPlayer(args[0]);
        if (requester != null) {
            tpaCommand.denyTeleport(player, requester);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (sender instanceof Player player && args.length == 1) {
            return tpaCommand.getTeleportRequests().getOrDefault(player, Collections.emptyList())
                    .stream().map(Player::getName).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        LanguageConfig.builder().buildAndSend(getPlugin().getMainConfig().getLanguageConfig().getPlayerOnly(),sender,true);
    }
}
