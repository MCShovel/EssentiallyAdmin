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
}
