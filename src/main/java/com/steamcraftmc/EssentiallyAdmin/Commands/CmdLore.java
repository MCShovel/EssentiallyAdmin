package com.steamcraftmc.EssentiallyAdmin.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

import net.md_5.bungee.api.ChatColor;

public class CmdLore extends BaseCommand {
    public CmdLore(MainPlugin plugin) {
        super(plugin, "lore", 1, 255);
    }

	@Override
    @SuppressWarnings("deprecation")
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {

		final ItemStack item = player.getItemInHand();
        if (item == null || item.getType().isBlock() || item.getItemMeta() == null) {
            player.sendMessage(plugin.Config.get("messages.modify-invalid", "&cYou can not modify that item."));
            return true;
        }

        int lineNum;
        String text;
        try {
        	lineNum = Integer.parseInt(args[0]);
        	args[0] = "";
        	text = String.join(" ", args).trim();
        } catch (Exception ex) {
        	return false;
        }
        
        if (lineNum < 1 || lineNum > 4) {
        	return false;
        }
        lineNum --;
        
        ItemMeta meta = item.getItemMeta().clone();
    	ArrayList<String> lore = new ArrayList<String>();
    	if (meta.getLore() != null) {
    		lore.addAll(meta.getLore());
    	}
    	while(lore.size() <= lineNum) {
    		lore.add("");
    	}
    	lore.set(lineNum, ChatColor.translateAlternateColorCodes('&', text));
    	meta.setLore(lore);
    	
        item.setItemMeta(meta);
        player.setItemInHand(item);
		
        player.sendMessage(plugin.Config.get("messages.modified", "&6Item modified."));
        return true;
	}
}
