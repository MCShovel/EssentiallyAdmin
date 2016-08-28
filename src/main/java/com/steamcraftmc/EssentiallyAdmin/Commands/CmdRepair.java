package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

public class CmdRepair extends BaseCommand {
    public CmdRepair(MainPlugin plugin) {
        super(plugin, "repair");
    }

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {

        @SuppressWarnings("deprecation")
		final ItemStack item = player.getItemInHand();
        if (item == null || item.getType().isBlock() || item.getDurability() == 0 || item.getType().getMaxDurability() < 1) {
            player.sendMessage(plugin.Config.get("messages.repair-invalid", "&cYou can not repair that item."));
            return true;
        }

        if (!item.getEnchantments().isEmpty() && !player.hasPermission("essentials.repair.enchanted")) {
            player.sendMessage(plugin.Config.get("messages.repair-invalid", "&cYou can not repair that item."));
            return true;
        }
		
        item.setDurability((short) 0);
        player.sendMessage(plugin.Config.get("messages.repair", "&6Item repaired."));
        return true;
	}
}
