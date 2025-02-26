package com.toonystank.rolleriteEssentials.utils;

import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

@Getter
public class LanguageConfig extends FileConfig {

    private String prefix;
    private String noPermission;
    private String playerOnly;
    private String consoleOnly;
    private String playerNotFound;

    public LanguageConfig() throws IOException {
        super("language.yml",false,true);
        load();
    }

    public void load() throws IOException {
        prefix = getString("prefix","&7[&bRolleriteEssentials&7] ");
        noPermission = getString("no-permission","&cYou do not have permission to do this.");
        playerOnly = getString("player-only","&cOnly players can use this command.");
        consoleOnly = getString("console-only","&cOnly console can use this command.");
        playerNotFound = getString("player-not-found","&cPlayer not found.");
    }

    @Override
    public void reload() throws IOException {
        super.reload();
        load();
    }

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public static class MessageBuilder {
        private String parma;
        private String error;
        private String player;
        private String amount;

        public MessageBuilder setParma(String parma) {
            this.parma = parma;
            return this;
        }

        public MessageBuilder setError(String error) {
            this.error = error;
            return this;
        }

        public MessageBuilder setPlayer(String player) {
            this.player = player;
            return this;
        }
        public MessageBuilder setPlayer(List<String> player) {
            if (player.size() > 1) this.player = "all";
            this.player = player.getFirst();
            return this;
        }


        public MessageBuilder setAmount(String amount) {
            this.amount = amount;
            return this;
        }


        public String build(String template) {
            return replace(template, parma, error, player, amount);
        }
        public String buildAndSend(String template, CommandSender player, boolean includePrefix) {
            String replaced = build(template);
            if (includePrefix) {
                replaced = RolleriteEssentials.getInstance().getMainConfig().getLanguageConfig().getPrefix() + replaced;
            }
            MessageUtils.sendMessage(player,replaced);
            return replaced;
        }
    }

    private static String replace(String string, @Nullable String parma, @Nullable String error, @Nullable String player, @Nullable String amount) {
        if (string == null) {
            MessageUtils.warning("Sending an empty message. String is null with param: " + parma + ", " + error + ", " + player +  ", " + amount);
            return "";
        }
        if (parma != null) {
            if (string.contains("{parma}")) string = string.replace("{parma}", parma);
        }
        if (error != null) {
            if (string.contains("{error}")) string = string.replace("{error}", error);
        }
        if (player != null) {
            if (string.contains("{player}")) string = string.replace("{player}", player);
        }
        if (amount != null) {
            if (string.contains("{amount}")) string = string.replace("{amount}", amount);
        }
        return string;
    }

}
