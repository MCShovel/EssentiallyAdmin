package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

import net.md_5.bungee.api.ChatColor;

public class CmdRename extends BaseCommand {
    public CmdRename(MainPlugin plugin) {
        super(plugin, "rename", 0, 255);
    }

	@Override
    @SuppressWarnings("deprecation")
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {

		final ItemStack item = player.getItemInHand();
        if (item == null || item.getType().isBlock() || item.getItemMeta() == null) {
            player.sendMessage(plugin.Config.get("messages.modify-invalid", "&cYou can not modify that item."));
            return true;
        }

        ItemMeta meta = item.getItemMeta().clone();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String.join(" ", args)));

        item.setItemMeta(meta);
        player.setItemInHand(item);
		
        player.sendMessage(plugin.Config.get("messages.modified", "&6Item modified."));
        return true;
	}
}
