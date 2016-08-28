package com.steamcraftmc.EssentiallyAdmin.Commands;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyAdmin.MainPlugin;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.logging.Level;


public class CmdGC extends BaseCommand {
    public CmdGC(MainPlugin plugin) {
        super(plugin, "gc");
    }

    @Override
    protected boolean doConsoleCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) throws Exception {
        sender.sendMessage(plugin.Config.format("messages.uptime", "&6The server has been running for &f{uptime}&6.",
        		"uptime", formatDateDiff(System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime())));
        sender.sendMessage(plugin.Config.format("messages.memory", 
        		"&6GC Used Memory &f{gcused}&7MB&6, Free &f{gcfree}&7MB&6, Max &f{gcmax}&7MB&6.",
        		"gcused", ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024),
        		"gcmax", (Runtime.getRuntime().maxMemory() / 1024 / 1024),
        		"gctotal", (Runtime.getRuntime().totalMemory() / 1024 / 1024),
        		"gcfree", (Runtime.getRuntime().freeMemory() / 1024 / 1024)
        		));

        List<World> worlds = plugin.getServer().getWorlds();
        for (World w : worlds) {
            String worldType;
            switch (w.getEnvironment()) {
                case NETHER:
                    worldType = "Nether";
                    break;
                case THE_END:
                    worldType = "The End";
                    break;
				default:
					worldType = "World";
					break;
            }

            int loadedChunks = 0;
            int tileEntities = 0;
            try {
                for (Chunk chunk : w.getLoadedChunks()) {
                	loadedChunks ++;
                    tileEntities += chunk.getTileEntities().length;
                }
            } catch (java.lang.ClassCastException ex) {
            	ex.printStackTrace();
                plugin.log(Level.SEVERE, "Corrupted chunk data on world " + w);
            }

            sender.sendMessage(plugin.Config.format("messages.gcworld", 
            		"&6{worldType} &f{world}&6 has &f{chunks}&6 chunks, &f{entities}&6 entities, &f{etiles}&6 etiles loaded.",
            		"world", w.getName(),
            		"worldType", worldType,
            		"chunks", loadedChunks,
            		"entities", w.getEntities().size(),
            		"etiles", tileEntities
            		));
        }
    	return true;
    }
    
	@Override
	protected boolean doPlayerCommand(Player sender, Command cmd, String commandLabel, String[] args) throws Exception {
		return doConsoleCommand(sender, cmd, commandLabel, args);
    }

	private Object formatDateDiff(long diffInMillis) {

		long diffInSeconds = Math.max(60L, diffInMillis / 1000L);
	    //long sec = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
	    long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
	    long hours = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
	    long days = (diffInSeconds = (diffInSeconds / 24));

	    StringBuilder sb = new StringBuilder();
	    if (days > 0) {
	    	sb.append(days);
	    	sb.append(" day");
	    	if (days > 1) sb.append('s');
	    	if (hours + min > 0) sb.append(", ");
	    }
	    if (hours > 0) {
	    	sb.append(hours);
	    	sb.append(" hour");
	    	if (hours > 1) sb.append('s');
	    	if (min > 0) sb.append(" and ");
	    }
	    if (min > 0) {
	    	sb.append(min);
	    	sb.append(" minute");
	    	if (min > 1) sb.append('s');
	    }
    	return sb.toString();
	}
}
