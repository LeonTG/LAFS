package com.leontg77.lafs.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import com.leontg77.lafs.Main;
import com.leontg77.lafs.Utils;
import com.leontg77.lafs.listeners.ClickListener;

/**
 * LAFS command class.
 * 
 * @author LeonTG77
 */
public class LAFSCommand implements CommandExecutor {
	private ClickListener listener = new ClickListener();
	
	private static final String USAGE = Main.PREFIX + "Usage: /lafs <enable|disable>";
	private static final String PLUGIN_NAME = "LAFS";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("lafs.manage")) {
			sender.sendMessage(Main.NO_PERM_MSG);
			return true;
		}
		
		if (args.length == 0) {
			sender.sendMessage(USAGE);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("enable")) {
			if (Main.enabled) {
				sender.sendMessage(Main.PREFIX + PLUGIN_NAME + " is already enabled.");
				return true;
			}

			Utils.broadcast(Main.PREFIX + PLUGIN_NAME + " has been enabled.");
			
			Bukkit.getPluginManager().registerEvents(listener, Main.plugin);
			Main.enabled = true;
			return true;
		} 

		if (args[0].equalsIgnoreCase("disable")) {
			if (!Main.enabled) {
				sender.sendMessage(Main.PREFIX + PLUGIN_NAME + " is not enabled.");
				return true;
			}
			
			Utils.broadcast(Main.PREFIX + PLUGIN_NAME + " has been disabled.");
			
			HandlerList.unregisterAll(listener);
			Main.enabled = false;
			return true;
		} 
		
		sender.sendMessage(USAGE);
		return true;
	}
}