package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

public class CmdFeed extends BaseCommand {

	public CmdFeed(MainPlugin plugin) {
		super(plugin, "feed", 0, 1);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {

		Player target = player;
        if (args.length > 0) {
        	if (!player.hasPermission("essentials.feed.others")) {
        		player.sendMessage(plugin.Config.NoAccess());
	            return true;
        	}
        	target = player.getServer().getPlayer(args[0]);
        	if (target == null) {
            	player.sendMessage(plugin.Config.PlayerNotFound(args[0]));
                return true;
        	}
        }

        if (target.getHealth() == 0) {
    		player.sendMessage(plugin.Config.get("messages.feed-dead", "&4Unable to feed the dead."));
            return true;
        }

        target.setFoodLevel(20);
        target.setSaturation(10);
		target.sendMessage(plugin.Config.get("messages.feed", "&4You have been satiated."));

		if (!player.equals(target)) {
        	player.sendMessage(
                	plugin.Config.format("messages.feed-other", "&6The player &3{name}&6 has been fed.", "name", target.getName())
				);
        }
		return true;
	}

}
