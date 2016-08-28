package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

public class CmdBurn extends BaseCommand {
    public CmdBurn(MainPlugin plugin) {
        super(plugin, "burn", 1, 2);
    }

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
        	player.sendMessage(plugin.Config.PlayerNotFound(args[0]));
        	return true;
        }

        int seconds = 30;
        if (args.length > 1 && args[1].matches("^\\d+$")) {
        	seconds = Integer.parseInt(args[1]);
        }

        target.setFireTicks(seconds * 20);
    	player.sendMessage(plugin.Config.format("messages.burn", "&cPlayer has been set on fire."));
        return true;
	}
}
