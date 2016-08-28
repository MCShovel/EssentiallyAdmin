package com.steamcraftmc.EssentiallyAdmin.Commands;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

public class CmdFly extends BaseCommand {

	Map<String, ConfigurationSection> allKits;

	public CmdFly(MainPlugin plugin) {
		super(plugin, "fly", 0, 1);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {
		Player target = player;
        if (args.length > 0) {
        	if (!player.hasPermission("essentials.fly.others")) {
        		player.sendMessage(plugin.Config.NoAccess());
	            return true;
        	}
        	target = Bukkit.getPlayer(args[0]);
        	if (target == null) {
        		player.sendMessage(plugin.Config.PlayerNotFound(args[0]));
                return true;
        	}
        }

        if (target.getHealth() == 0) {
    		player.sendMessage(plugin.Config.get("messages.fly-dead", "&4Unable to make the dead fly."));
            return true;
        }

        boolean enabled = !target.getAllowFlight();

        target.setFallDistance(0f);
        target.setAllowFlight(enabled);

        if (!target.getAllowFlight()) {
        	target.setFlying(false);
        }

        if (enabled) {
    		target.sendMessage(plugin.Config.get("messages.fly-enabled", "&6You have been given the gift of flight!"));
        }
        else {
    		target.sendMessage(plugin.Config.get("messages.fly-disabled", "&6Flight is no longer within your power."));
        }
        
        if (!player.equals(target)) {
        	player.sendMessage(
        			enabled
        			? plugin.Config.format("messages.fly-other-enabled", "&6Fly enabled for &3{name}&6.", "name", target.getName())
                	: plugin.Config.format("messages.fly-other-disabled", "&6Fly disabled for &3{name}&6.", "name", target.getName())
				);
        }
        return true;
	}

}
