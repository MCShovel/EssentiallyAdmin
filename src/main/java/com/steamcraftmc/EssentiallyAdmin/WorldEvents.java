package com.steamcraftmc.EssentiallyAdmin;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.steamcraftmc.EssentiallyAdmin.utils.AnvilColorSync;

public class WorldEvents implements Listener, Runnable {
	MainPlugin plugin;
	private final HashMap<Inventory, AnvilColorSync> _open;
	int _cleanTaskId;

	public WorldEvents(MainPlugin plugin) {
		this.plugin = plugin;
		this._open = new HashMap<Inventory, AnvilColorSync>();
	}

	public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        _cleanTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 60000, 60000);
	}

	public void stop() {
		plugin.getServer().getScheduler().cancelTask(_cleanTaskId);
		HandlerList.unregisterAll(this);
		for (AnvilColorSync s : _open.values())
			s.close();
		_open.clear();
	}

	@Override
	public void run() {
		ArrayList<Inventory> closed = new ArrayList<Inventory>(); 
		for (Inventory inv : _open.keySet()) {
			try {
				if (inv.getViewers().size() == 0) {
					closed.add(inv);
				}
			}
			catch(Exception e) {
				closed.add(inv);
			}
		}
		for (Inventory inv : closed) {
			AnvilColorSync sync = _open.remove(inv);
			if (sync != null) {
				sync.close();
			}
		}
	}
	
	private AnvilColorSync getColorSync(Inventory inv, boolean attach) {
		AnvilColorSync sync = _open.get(inv);
		if (sync == null && attach && inv instanceof AnvilInventory) {
			sync = new AnvilColorSync(plugin, (AnvilInventory)inv);
			_open.put((AnvilInventory)inv, sync);
		}
		return sync;
	}
	
    @EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
    	if (event.getInventory() instanceof AnvilInventory) {
    		getColorSync(event.getInventory(), true).open(event.getPlayer());
    	}
	}

    @EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory() instanceof AnvilInventory) {
			AnvilColorSync sync = _open.remove((AnvilInventory)event.getInventory());
			if (sync != null) {
				sync.close();
			}
		}
	}
	
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
    	
        if (event.getInventory() instanceof AnvilInventory) {
			AnvilColorSync sync = getColorSync(event.getInventory(), true);
			
			ItemStack replace = null;
        	int slot = event.getRawSlot();
        	if (slot == 0) {
        		// new base item
        		replace = sync.changeBaseItem(event.getWhoClicked(), event.getCurrentItem());
        	}
        	else if (slot == 1) {
        		// new modifier item
        		replace = sync.changeModifierItem(event.getWhoClicked(), event.getCurrentItem());
        	}
        	else if (slot == 2) {
            	// take output item
        		replace = sync.getFinalItem(event.getWhoClicked(), event.getCurrentItem());
            }
        	if (replace != null) {
        		event.setCurrentItem(replace);
        	}
        }
    }
}
