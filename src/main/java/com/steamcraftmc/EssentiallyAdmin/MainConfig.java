package com.steamcraftmc.EssentiallyAdmin;

import com.steamcraftmc.EssentiallyAdmin.utils.BaseYamlSettingsFile;

public class MainConfig  extends BaseYamlSettingsFile {
	public MainConfig(MainPlugin plugin) { super(plugin, "config.yml"); }

	public String NoAccess() {
		return get("messages.no-access", "&4You do not have permission to this command.");
	}

	public String PlayerNotFound(String player) {
		return format("message.player-not-found", "&cPlayer not found.", "player", String.valueOf(player));
	}
}
