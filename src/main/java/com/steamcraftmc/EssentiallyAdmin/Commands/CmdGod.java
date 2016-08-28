package com.steamcraftmc.EssentiallyAdmin.Commands;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

public class CmdGod extends BaseCommand implements Listener {
    public CmdGod(MainPlugin plugin) {
		super(plugin, "god");
	}

    final Set<UUID> _gods = new HashSet<UUID>();

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public void stop() {
    	HandlerList.unregisterAll(this);
    }
    
    private boolean isGod(Player player) {
    	return _gods.contains(player.getUniqueId());
    }
    
    private void setGod(Player player, boolean enable, boolean persist) {
    	if (enable) {
    		_gods.add(player.getUniqueId());
    		player.setInvulnerable(true);
    		player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setSaturation(10);
            player.setFireTicks(0);
    	}
    	else {
    		_gods.remove(player.getUniqueId());
    		player.setInvulnerable(false);
    	}
    	if (persist) {
    		plugin.Config.Write("gods." + player.getUniqueId().toString(), enable);
    	}
    }

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {

		boolean persist = true;
		Player target = player;
		if (args.length > 0) {
        	if (!player.hasPermission("essentials.god.others")) {
        		player.sendMessage(plugin.Config.NoAccess());
	            return true;
        	}
			persist = false;
			target = plugin.getServer().getPlayer(args[0]);
			if (target == null) {
            	player.sendMessage(plugin.Config.PlayerNotFound(args[0]));
            	return true;
			}
		}
		
        boolean enabled = !isGod(target);
        setGod(target, enabled, persist);

        target.sendMessage(
    			enabled
    			? plugin.Config.format("messages.god-enabled", "&6God Mode &2enabled&6.")
            	: plugin.Config.format("messages.god-disabled", "&6God Mode &4disabled&6.")
			);
        
        if (!player.equals(target)) {
        	player.sendMessage(
        			enabled
        			? plugin.Config.format("messages.god-other-enabled", "&6God Mode &2enabled&6 for &3{name}&6.", "name", target.getName())
                	: plugin.Config.format("messages.god-other-disabled", "&6God Mode &4disabled&6 for &3{name}&6.", "name", target.getName())
				);
        } 
        return true;
    }
	
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	if (plugin.Config.getRaw("gods." + player.getUniqueId().toString()) == "true") {
    		if (!player.hasPermission("essentials.god")) {
        		setGod(player, false, true);
    		} else {
    			setGod(player, true, false);
    		}
    	} 
    	else {
    		setGod(player, false, false);
    	}
	}

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && isGod((Player) event.getEntity())) {
            final Player player = (Player) event.getEntity();
            player.setFireTicks(0);
            player.setRemainingAir(player.getMaximumAir());
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityCombust(final EntityCombustEvent event) {
        if (event.getEntity() instanceof Player && isGod((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }
    
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = ((Player) event.getEntity());
            if (isGod(player)) {
                player.setFoodLevel(20);
                player.setSaturation(10);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeathEvent(final PlayerDeathEvent event) {
    	Player player = event.getEntity();
    	if (isGod(player) || player.hasPermission("essentials.god.keepInventory")) {
	    	event.setKeepInventory(true);
	    	event.setDroppedExp(0);
	    	event.setKeepLevel(true);
	    	event.setDeathMessage(null);
    	}
    }
}
