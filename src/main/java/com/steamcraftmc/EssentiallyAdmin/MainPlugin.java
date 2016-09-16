package com.steamcraftmc.EssentiallyAdmin;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
 
public class MainPlugin extends JavaPlugin {
	public final   Logger  _logger;
	private WorldEvents _listener;
	public Boolean _exLogging;
	public final MainConfig Config;
	public com.steamcraftmc.EssentiallyAdmin.Commands.CmdGod god;
	public com.steamcraftmc.EssentiallyAdmin.Commands.CmdFireball fb;

	public MainPlugin() {
		_exLogging = true;
		_logger = getLogger();
		_logger.setLevel(Level.ALL);
		_logger.log(Level.CONFIG, "Plugin initializing...");
		
		Config = new MainConfig(this);
		Config.load();
	}

	public void log(Level level, String text) {
		_logger.log(Level.INFO, text);
	}

    @Override
    public void onEnable() {
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdFeed(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdHeal(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdFixLight(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdFly(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdGameMode(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdBurn(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdLightning(this);
        fb = new com.steamcraftmc.EssentiallyAdmin.Commands.CmdFireball(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdGC(this);
        god = new com.steamcraftmc.EssentiallyAdmin.Commands.CmdGod(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdRepair(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdRename(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdLore(this);
        new com.steamcraftmc.EssentiallyAdmin.Commands.CmdSpeed(this);

    	_listener = new WorldEvents(this);
        _listener.start();
        fb.start();
        god.start();
        log(Level.INFO, "Plugin listening for events.");
    }

    @Override
    public void onDisable() {
        fb.stop();
        god.stop();
        _listener.stop();
    	HandlerList.unregisterAll(_listener);
    }

}
