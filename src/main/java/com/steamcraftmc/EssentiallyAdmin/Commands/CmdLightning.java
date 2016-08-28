package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;
import com.steamcraftmc.EssentiallyAdmin.utils.BlockUtil;

import java.util.Set;

public class CmdLightning extends BaseCommand {

    public CmdLightning(MainPlugin plugin) {
        super(plugin, "lightning", 0, 1);
    }

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
		Player pTarget = null;
		Location target;
        
        if (args.length > 0) {
        	pTarget = plugin.getServer().getPlayer(args[0]);
            if (pTarget == null) {
            	player.sendMessage(plugin.Config.PlayerNotFound(args[0]));
            	return true;
            }
            target = pTarget.getLocation();
        }
        else {
        	Set<Material> transparent = BlockUtil.getTransparentBlocks();
        	Block b = player.getTargetBlock(transparent, 100);
        	if (b == null) {
            	player.sendMessage(plugin.Config.get("messages.lightning-too-far", "&cThat is too far away."));
            	return true;
        	}
        	target = b.getLocation();
        }
        
        target.getWorld().strikeLightningEffect(target);
        if (pTarget != null) {
            pTarget.sendMessage(plugin.Config.format("messages.smitten", "&6You have been smitten."));
            player.sendMessage(plugin.Config.format("messages.lightning", "&6You have unleashed the power of Thor!"));
        }

        return true;
	}
}
