package com.steamcraftmc.EssentiallyAdmin.utils;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;

public class BlockUtil {

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public static Set<Material> getTransparentBlocks() {
		final HashSet<Material> _transparent = new HashSet<Material>();
		for (Material m : Material.values()) {
			if (m.isTransparent()) {
				_transparent.add(m);
			}
		}
		return _transparent;
	}

	public static String properCase(String text) {
		char[] chars = text.toCharArray();
		boolean makeUpper = true;
		for (int ix = 0; ix < chars.length; ix++) {
			char ch = makeUpper ? Character.toUpperCase(chars[ix]) : Character.toLowerCase(chars[ix]);
			makeUpper = !Character.isLetter(ch);
			chars[ix] = makeUpper ? ' ' : ch;
		}

		return new String(chars).replaceAll("\\s\\s+", " ").trim();
	}
	
	public static String itemName(int materialId) {
		String tmp = null;
		try {
			net.minecraft.server.v1_11_R1.ItemStack stack = new net.minecraft.server.v1_11_R1.ItemStack(net.minecraft.server.v1_11_R1.Item.getById(materialId));
			tmp = stack == null ? null : stack.getName();
		}
		catch (Exception e) {}
		if (tmp == null || tmp.length() == 0) {
			tmp = Material.getMaterial(materialId).name();
		}
		tmp = properCase(tmp);
		return tmp;
	}
	
}
