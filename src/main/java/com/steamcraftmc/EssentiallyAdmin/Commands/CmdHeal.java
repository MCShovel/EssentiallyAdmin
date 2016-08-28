package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

public class CmdHeal extends BaseCommand {

	public CmdHeal(MainPlugin plugin) {
		super(plugin, "heal", 0, 1);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {

		Player target = player;
        if (args.length > 0) {
        	if (!player.hasPermission("essentials.heal.others")) {
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
    		player.sendMessage(plugin.Config.get("messages.heal-dead", "&4Unable to return the dead to life."));
            return true;
        }

        target.setHealth(target.getMaxHealth());
        target.setFoodLevel(20);
        target.setSaturation(10);
        target.setFireTicks(0);
		target.sendMessage(plugin.Config.get("messages.heal", "&6You have been healed."));
        for (PotionEffect effect : target.getActivePotionEffects()) {
            target.removePotionEffect(effect.getType());
        }

        if (!player.equals(target)) {
        	player.sendMessage(
                	plugin.Config.format("messages.heal-other", "&6The player &3{name}&6 has been healed.", "name", target.getName())
				);
        }
    	return true;
	}

}
