package com.toonystank.rolleriteEssentials.Modules;

import com.toonystank.rolleriteEssentials.Modules.managers.BaseModule;
import com.toonystank.rolleriteEssentials.RolleriteEssentials;
import com.toonystank.rolleriteEssentials.utils.LanguageConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.io.IOException;
import java.util.List;

public class FixCommand extends BaseModule {

    private String fixedItemMessage;
    private String noItemInHandMessage;
    private String notRepairableMessage;

    public FixCommand(RolleriteEssentials plugin) {
        super(plugin, "fix", new Command("fix"
                ,true
                , false
                , "Repairs the item in your hand to full durability"
                , "/fix"
                , "RolleriteEssentials.fix",
                List.of("repair")));
    }

    @Override
    public void setLanguages() throws IOException {
        fixedItemMessage = super.getLanguageConfig().getString("fix.success", "&aYour item has been fully repaired.");
        noItemInHandMessage = super.getLanguageConfig().getString("fix.no_item", "&cYou must be holding an item to repair it.");
        notRepairableMessage = super.getLanguageConfig().getString("fix.not_repairable", "&cThis item cannot be repaired.");
    }

    @Override
    public void execute(Player player, String[] args) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.isEmpty() || item.getType().isAir()) {
            LanguageConfig.builder().buildAndSend(noItemInHandMessage,player,true);
            return;
        }

        if (!(item.getItemMeta() instanceof Damageable damageableMeta)) {
            LanguageConfig.builder().buildAndSend(notRepairableMessage,player,true);
            return;
        }
        damageableMeta.setDamage(0);
        item.setItemMeta(damageableMeta);

        LanguageConfig.builder().buildAndSend(fixedItemMessage,player,true);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
        LanguageConfig.builder().buildAndSend(getLanguageConfig().getPlayerOnly(), sender, true);
    }
}
