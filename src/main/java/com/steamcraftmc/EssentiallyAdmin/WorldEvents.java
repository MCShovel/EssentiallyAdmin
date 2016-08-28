package com.steamcraftmc.EssentiallyAdmin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WorldEvents implements Listener {
	MainPlugin plugin;

	public WorldEvents(MainPlugin plugin) {
		this.plugin = plugin;
	}

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar) {
            	char ch = b[i + 1];
            	if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || "lnor".indexOf(ch) >= 0) {
					b[i] = ChatColor.COLOR_CHAR;
					b[i+1] = Character.toLowerCase(ch);
                }
            }
        }
        return new String(b);
    }

	@EventHandler(priority = EventPriority.HIGH)
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		if (player != null && player.hasPermission("essentials.signs.color")) {
			for (int line = 0; line <= 3; line++) {
				event.setLine(line, translateAlternateColorCodes('&', event.getLine(line)));
			}
		}
	}
}
