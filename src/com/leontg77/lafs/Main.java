package com.leontg77.lafs;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.leontg77.lafs.commands.LAFSCommand;

/**
 * Main class of the plugin.
 * 
 * @author LeonTG77
 */
public class Main extends JavaPlugin {
	public static boolean enabled = false;
	public static Main plugin;
	
	public static final String NO_PERM_MSG = "§cYou don't have permission.";
	public static final String PREFIX = "§c§lKillReveal §8» §7";
	
	/**
	 * Get the instance of the class.
	 * 
	 * @return The instance.
	 */
	public static Main getInstance() {
		return plugin;
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " is now disabled.");
		
		plugin = null;
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " v" + file.getVersion() + " is now enabled.");
		getLogger().info("Plugin made by LeonTG77.");
		
		plugin = this;
		
		getCommand("lafs").setExecutor(new LAFSCommand());
	}
	
	/**
	 * Set the size of the teams to combine.
	 * 
	 * @param size The new size.
	 */
	public void setSize(int size) {
		getConfig().set("teamsizeToCombine", size);
		saveConfig();
	}
	
	/**
	 * Get the size of the teams to combine.
	 * 
	 * @return The size.
	 */
	public int getSize() {
		return getConfig().getInt("teamsizeToCombine", 1);
	}
}