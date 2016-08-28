package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;
import com.steamcraftmc.EssentiallyAdmin.utils.ChunkWrap;

public class CmdFixLight extends BaseCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFixLight(MainPlugin plugin) {
		super(plugin, "fixlight", 1, 1);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
		
		if (args.length == 0 || !args[0].matches("^\\d+$")) {
			return false;
		}
		
		// Args
		int radius = Integer.parseInt(args[0]);
		if (radius < 0)
		{
			player.sendMessage(plugin.Config.get("messages.neg-radius", "&4Radius may not be a negative value."));
			return true;
		}
		int maxRadius = plugin.Config.getInt("lighting.maxradius", 8);
		if (radius > maxRadius)
		{
			player.sendMessage(plugin.Config.format("messages.radius-too-large", "&4The maxium radius allowed is &6{max}&4.", "max", maxRadius));
			return true;
		}
		
		Chunk origin = player.getLocation().getChunk();
		int originX = origin.getX();
		int originZ = origin.getZ();
		World world = player.getWorld();
		
		int side = (1 + radius * 2);
		int target = side * side;
		player.sendMessage(plugin.Config.get("messages.fixlight", "&6Relighting the world around you."));
		player.sendMessage(plugin.Config.format("messages.fixlight-size", 
				"&6Radius &7{radius}&6, Side &7{side}&6, Chunks &7{chunks}&6.", 
				"radius", radius, 
				"side", side, 
				"chunks", target));
		
		// Apply
		for (int deltaX = -radius; deltaX <= radius; deltaX++)
		{
			for (int deltaZ = -radius; deltaZ <= radius; deltaZ++)
			{
				int x = originX + deltaX;
				int z = originZ + deltaZ;
				new ChunkWrap(world, x, z).recalcLightLevel();
			}	
		}
		
		// Post Inform
		player.sendMessage(plugin.Config.format("messages.fixlight-complete", "&6Chunk relight complete."));
		return true;
	}
	
}
