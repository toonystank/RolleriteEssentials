package com.toonystank.rolleriteEssentials.Modules;

import com.toonystank.rolleriteEssentials.Modules.managers.BaseModule;
import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import com.toonystank.rolleriteEssentials.gui.PlayerGUI;
import com.toonystank.rolleriteEssentials.utils.LanguageConfig;
import com.toonystank.rolleriteEssentials.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class TrashCommand extends BaseModule {

    private String trashTitle = "Trash Can";
    private String trashCanMessage = "Opening the trash can. Anything you put in here will be deleted.";

    public TrashCommand(RolleriteEssentials plugin) {
        super(plugin, "trash", new Command("trash",
                false,
                false,
                "Open the trash GUI to dispose of items",
                "/trash",
                "rollerite.trash",
                List.of("dispose")));
    }

    @Override
    public void setLanguages() throws IOException {
        trashTitle = getPlugin().getMainConfig().getLanguageConfig().getString("trash.title", trashTitle);
        trashCanMessage = getPlugin().getMainConfig().getLanguageConfig().getString("trash.trashCanMessage", trashCanMessage);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        LanguageConfig.builder().buildAndSend(getPlugin().getMainConfig().getLanguageConfig().getPlayerOnly(), sender,true);
    }

    @Override
    public void execute(Player player, String[] args) {
        openTrashGUI(player);
    }

    private void openTrashGUI(Player player) {
        PlayerGUI trashInventory = new PlayerGUI(MessageUtils.format(trashTitle));
        trashInventory.setCloseAction((event, inventory) -> inventory.getInventory().clear());
        trashInventory.open(player);
        LanguageConfig.builder().buildAndSend(trashCanMessage, player,true);
    }
}