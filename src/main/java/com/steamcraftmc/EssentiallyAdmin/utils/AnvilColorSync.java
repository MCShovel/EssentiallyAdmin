package com.steamcraftmc.EssentiallyAdmin.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permissible;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

import net.md_5.bungee.api.ChatColor;

public class AnvilColorSync implements Runnable {
	final MainPlugin plugin;
	final AnvilInventory inventory;
	
	Integer _taskId;

	Permissible operator;
	String prevName; 
	
	public AnvilColorSync(MainPlugin plugin, AnvilInventory inv) {
		this.plugin = plugin;
		this.inventory = inv;
		
		this.operator = null;
		this.prevName = null;
		
		_taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 5, 5);
	}

	@Override
	public void run() {
		if (inventory.getViewers().size() == 0) {
			this.close();
			return;
		}

		this.onCheckRename();
	}

	/* ********************************************************************* */

	private boolean isInputEnchanted() {
		ItemStack inputItem = inventory.getItem(0);
		if (inputItem != null) {
			return inputItem.getEnchantments().size() > 0;
		}
		return false;
	}
	
	private boolean inputHasLore() {
		ItemStack inputItem = inventory.getItem(0);
		if (inputItem != null) {
			ItemMeta meta = inputItem.getItemMeta();
			if (meta != null && meta.hasLore()) {
				return true;
			}
		}
		return false;
	}
	
	private String getInputItemName() {
		ItemStack inputItem = inventory.getItem(0);
		return getNameFromItem(inputItem);
	}
	
	private String getResultItemName() {
		ItemStack inputItem = inventory.getItem(2);
		return getNameFromItem(inputItem);
	}
	
	private String getNameFromItem(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) {
			return null;
		}
		
		String name = null;
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			name = meta.getDisplayName();
		}
		
		if (name == null) {
			name = BlockUtil.itemName(item.getTypeId());
		}
		
		return name;
	}
	

	public ItemStack getNewItem(ItemStack outputItem) {
		
		String origName = getInputItemName();
		if (operator == null || origName == null || outputItem == null || outputItem.getType() == Material.AIR) {
			return null;
		}

		ItemMeta outputItemMeta = outputItem.getItemMeta();
		if (outputItemMeta == null) {
			return null;
		}

		outputItemMeta = outputItemMeta.clone();
		String fixedName = outputItemMeta.getDisplayName();

		// Case: special permission required to rename items with lore...
		if (inputHasLore() && !operator.hasPermission("essentials.anvilcolor.rename")) {
			outputItemMeta.setDisplayName(prevName = origName);
			outputItem.setItemMeta(outputItemMeta);
			return outputItem;
		}
		
		// Case: user removed all text to default name (or it never had one)
		if (fixedName == null || fixedName.equals(origName)) {
			return null;
		}

		// Case: Operator does not have permissions to use colors, strip them...
		if (!operator.hasPermission("essentials.anvilcolor.use")) {
			fixedName = ChatColor.stripColor(fixedName).replaceAll("\u00a7", "");
		}
		
		// Case: edit a colored name strips the color character, but should not...
		if (fixedName.equals(origName.replaceAll("\u00a7", ""))) {
			fixedName = origName;
		}
		
		// Case: editing a colored name back to the original is allowed...
		if (ChatColor.stripColor(fixedName).equals(ChatColor.stripColor(origName))) {
			fixedName = origName;
		}
		
		// Case: user is allowed to use colors, so colorize...
		if (operator.hasPermission("essentials.anvilcolor.use")) {
			fixedName = ChatColor.translateAlternateColorCodes('&', fixedName);
		}

		// Case: user wants to 'sign' the original name...
		if (fixedName.equals("+") && plugin.Config.getBoolean("anvilSigning.enabled", false)
				&& operator.hasPermission("essentials.anvilcolor.sign")) {

			String enchanted = isInputEnchanted() ? plugin.Config.get("anvilSigning.enchanted", "&o") : "";

			fixedName = plugin.Config.get("anvilSigning.color", "&3") + enchanted
					+ ChatColor.stripColor(origName);

			if (operator instanceof Player) {
				Player player = (Player)operator;
				plugin.log(Level.INFO, player.getName() + " is signing item " + outputItem.getType().toString());

				String dNow = new SimpleDateFormat("MMM dd yyyy").format(new Date());
				ArrayList<String> lore = new ArrayList<String>();
				for (String line : plugin.Config.getArray("anvilSigning.lore",
						new String[] { "&r&7Created By {player}", "&r&7On {date}" })) {
					lore.add(ChatColor.translateAlternateColorCodes('&', line)
							.replace("{player}", player.getDisplayName())
							.replace("{world}", player.getWorld().getName()).replace("{date}", dNow));
				}
				outputItemMeta.setLore(lore);
			}
		}

		this.prevName = fixedName;
		if (fixedName == outputItemMeta.getDisplayName()) {
			return null; // nothing changed.
		}

		outputItemMeta.setDisplayName(fixedName);
		outputItem.setItemMeta(outputItemMeta);
		return outputItem;
	}

	/* ********************************************************************* */
	
	public void open(HumanEntity player) {
		operator = player;
	}

	public void close() {
		if (this._taskId != null) {
			plugin.getServer().getScheduler().cancelTask(this._taskId);
			this._taskId = null;
			return;
		}
	}
	
	public void onCheckRename() {
		if (this.inventory == null || this.operator == null) {
			return;
		}
		
		String resultName = getResultItemName();
		if (resultName == null || resultName == this.prevName) {
			this.prevName = resultName;
			return;
		}
		
		this.prevName = resultName;
		ItemStack outputItem = inventory.getItem(2);
		outputItem = this.getNewItem(outputItem);
		if (outputItem != null) {
			inventory.setItem(2, outputItem);
		}
	}

	public ItemStack changeBaseItem(HumanEntity player, ItemStack currentItem) {
		operator = player;
		prevName = null;
		return null;
	}

	public ItemStack changeModifierItem(HumanEntity player, ItemStack currentItem) {
		operator = player;
		prevName = null;
		return null;
	}

	public ItemStack getFinalItem(HumanEntity player, ItemStack currentItem) {
		ItemStack result = null;
		operator = player;
		
		if (currentItem != null && currentItem.getType() != Material.AIR) {
			if (prevName != this.getNameFromItem(currentItem))
				result = getNewItem(currentItem);
		}
		
		prevName = null;
		return result;
	}
}
