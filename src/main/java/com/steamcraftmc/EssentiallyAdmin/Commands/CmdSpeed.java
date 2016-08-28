package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

public class CmdSpeed extends BaseCommand {
    public CmdSpeed(MainPlugin plugin) {
        super(plugin, "speed", 2, 2);
    }

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
        if (args.length != 2) {
			return false;
        }

		boolean isFly = false;
		String modeString = args[0];
        if (modeString.contains("fly") || modeString.equalsIgnoreCase("f")) {
        	isFly = true;
        } else if (modeString.contains("walk") || modeString.contains("run") || modeString.equalsIgnoreCase("w") || modeString.equalsIgnoreCase("r")) {
        	isFly = false;
        } else {
        	return false;
        }

		float speed = 1f;
		if (args[1].matches("^\\d+(\\.\\d+)?$")) {
			speed = Float.parseFloat(args[1]);
		} else {
			return false;
		}

        if (isFly) {
        	if (!player.hasPermission("essentials.speed.fly")) {
        		player.sendMessage(plugin.Config.NoAccess());
        		return true;
        	}
            player.setFlySpeed(getRealMoveSpeed(speed, isFly));
            player.sendMessage(plugin.Config.get("messages.speed-fly", "&6Your flying speed has been changed."));
        } else {
        	if (!player.hasPermission("essentials.speed.walk")) {
        		player.sendMessage(plugin.Config.NoAccess());
        		return true;
        	}
        	player.setWalkSpeed(getRealMoveSpeed(speed, isFly));
            player.sendMessage(plugin.Config.get("messages.speed-walk", "&6Your walking speed has been changed."));
        }
        return true;
    }

    private float getRealMoveSpeed(float userSpeed, final boolean isFly) {
        
        float maxSpeed = (float)plugin.Config.getInt("speed.max", 10);
        userSpeed = Math.min(maxSpeed, userSpeed);
        
        final float defaultSpeed = isFly ? 0.1f : 0.2f;
        if (userSpeed < 1f) {
            return defaultSpeed * userSpeed;
        } else {
            return ((userSpeed - 1) * defaultSpeed) + defaultSpeed;
        }
    }

}
