package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

public class CmdFireball extends BaseCommand implements Listener {
    final int speed = 2;
    
    public CmdFireball(MainPlugin plugin) {
        super(plugin, "fireball", 1);
    }

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
        Class<? extends Entity> type = Fireball.class;
        Projectile projectile;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("small")) {
                type = SmallFireball.class;
            } else if (args[0].equalsIgnoreCase("arrow")) {
                type = Arrow.class;
            } else if (args[0].equalsIgnoreCase("skull")) {
                type = WitherSkull.class;
            } else if (args[0].equalsIgnoreCase("egg")) {
                type = Egg.class;
            } else if (args[0].equalsIgnoreCase("snowball")) {
                type = Snowball.class;
            } else if (args[0].equalsIgnoreCase("expbottle")) {
                type = ThrownExpBottle.class;
            } else if (args[0].equalsIgnoreCase("large")) {
                type = LargeFireball.class;
            }
        }
        final Vector direction = player.getEyeLocation().getDirection().multiply(speed);
        projectile = (Projectile) player.getWorld().spawn(player.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), type);
        projectile.setShooter(player);
        projectile.setVelocity(direction);
        return true;
	}

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public void stop() {
    	HandlerList.unregisterAll(this);
    }

    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=false)
    public void onInteract(PlayerInteractEvent e) {

    	Player player = e.getPlayer();
    	if (player != null && player.hasPermission(this.permission)) {
	    	if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (e.getHand() == EquipmentSlot.HAND) {
					ItemStack item = e.getItem();
					if (item != null && item.getType() == Material.FIREBALL) {
				        Class<? extends Entity> type = Fireball.class;
				        Projectile projectile;
				        final Vector direction = player.getEyeLocation().getDirection().multiply(speed);
				        projectile = (Projectile) player.getWorld().spawn(player.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), type);
				        projectile.setShooter(player);
				        projectile.setVelocity(direction);
					}
				}
	    	}
    	}
    }
}
