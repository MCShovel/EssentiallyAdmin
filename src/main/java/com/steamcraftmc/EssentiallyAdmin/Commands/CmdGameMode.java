package com.steamcraftmc.EssentiallyAdmin.Commands;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

public class CmdGameMode extends BaseCommand {

	Map<String, ConfigurationSection> allKits;

	public CmdGameMode(MainPlugin plugin) {
		super(plugin, "gamemode", 0, 2);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {
        GameMode gameMode;
        
        int ix = 0;
        gameMode = matchGameMode(commandLabel);
        
        if (gameMode == null && args.length > 0) {
        	gameMode = matchGameMode(args[ix++]);
        }
        if (gameMode == null) {
        	return false;
        }
        
        Player target = player;
        if (args.length > ix) {
        	if (!player.hasPermission("essentials.gamemode.others")) {
        		player.sendMessage(plugin.Config.NoAccess());
        		return true;
        	}
        	target = Bukkit.getPlayer(args[ix]);
        	if (target == null) {
        		player.sendMessage(plugin.Config.PlayerNotFound(args[0]));
        		return true;
        	}
        }

        if (!player.hasPermission("essentials.gamemode.all") && !player.hasPermission("essentials.gamemode." + gameMode.name().toLowerCase())) {
    		player.sendMessage(plugin.Config.NoAccess());
            return true;
        }
        
 		target.setGameMode(gameMode);
 		target.sendMessage(plugin.Config.format("messages.gamemode", "&6You game mode has been changed.", "mode", gameMode.toString().toLowerCase()));

        if (!player.equals(target)) {
	    	player.sendMessage(
	    			plugin.Config.format("messages.gamemode-other", "&6Gamemode changed for &3{name}&6.", "name", target.getName())
				);
        }
 		return true;
	}
	

    private GameMode matchGameMode(String modeString) {
        GameMode mode = null;
               if (modeString.equalsIgnoreCase("gmc") || modeString.equalsIgnoreCase("creative") || modeString.equalsIgnoreCase("1") || modeString.equalsIgnoreCase("c")) {
            mode = GameMode.CREATIVE;
        } else if (modeString.equalsIgnoreCase("gms") || modeString.equalsIgnoreCase("survival") || modeString.equalsIgnoreCase("0") || modeString.equalsIgnoreCase("s")) {
            mode = GameMode.SURVIVAL;
        } else if (modeString.equalsIgnoreCase("gma") || modeString.equalsIgnoreCase("adventure") || modeString.equalsIgnoreCase("2") || modeString.equalsIgnoreCase("a")) {
            mode = GameMode.ADVENTURE;
        } else if (modeString.equalsIgnoreCase("gmp") || modeString.equalsIgnoreCase("spectator") || modeString.equalsIgnoreCase("3") || modeString.equalsIgnoreCase("p")) {
            mode = GameMode.SPECTATOR;
        } else {
    		return null;
        }
        return mode;
    }
}
