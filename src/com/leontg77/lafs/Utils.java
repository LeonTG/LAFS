package com.leontg77.lafs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Player utilities class.
 * <p>
 * Contains player related methods.
 * 
 * @author LeonTG77
 */
public class Utils {
	
	/**
	 * Get a list of players online.
	 * 
	 * @return A list of online players.
	 */
	public static List<Player> getPlayers() {
		return new ArrayList<Player>(Bukkit.getOnlinePlayers());
	}
	
	/**
	 * Gets an offline player by a name.
	 * <p>
	 * This is just because of the deprecation on <code>Bukkit.getOfflinePlayer(String)</code> 
	 * 
	 * @param name The name.
	 * @return the offline player.
	 */
	@SuppressWarnings("deprecation")
	public static OfflinePlayer getOfflinePlayer(String name) {
		return Bukkit.getOfflinePlayer(name);
	}
	
	/**
	 * Broadcasts a message to everyone online.
	 * 
	 * @param message the message.
	 */
	public static void broadcast(String message) {
		broadcast(message, null);
	}
	
	/**
	 * Broadcasts a message to everyone online with a specific permission.
	 * 
	 * @param message the message.
	 * @param permission the permission.
	 */
	public static void broadcast(String message, String permission) {
		for (Player online : getPlayers()) {
			if (permission != null && !online.hasPermission(permission)) {
				continue;
			}
			
			online.sendMessage(message);
		}
		
		String consoleMsg = message;

		consoleMsg = consoleMsg.replaceAll("§l", "");
		consoleMsg = consoleMsg.replaceAll("§o", "");
		consoleMsg = consoleMsg.replaceAll("§r", "");
		consoleMsg = consoleMsg.replaceAll("§m", "");
		consoleMsg = consoleMsg.replaceAll("§n", "");
		
		Bukkit.getLogger().info(consoleMsg);
	}
}