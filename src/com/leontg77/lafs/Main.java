package com.leontg77.lafs;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

/**
 * Main class of the UHC plugin.
 * @author LeonTG77
 */
@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener {
	private final Logger logger = Bukkit.getServer().getLogger();
	private boolean enabled = false;
	public static Main plugin;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is now disabled.");
		plugin = null;
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " v" + pdfFile.getVersion() + " is now enabled.");
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		plugin = this;
	}
	
	/**
	 * Get the UHC prefix with an ending color.
	 * @param endcolor the ending color.
	 * @return The UHC prefix.
	 */
	public static String prefix() {
		String prefix = "§d§lLAFS §8§l>> §7";
		return prefix;
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (!enabled) {
			return;
		}
		
		if (event.getRightClicked() instanceof Player) {
			Player player = event.getPlayer();
			Player clicked = (Player) event.getRightClicked();
			
			if (player.getScoreboard().getPlayerTeam(player) != null) {
				player.sendMessage(ChatColor.RED + "You are already on a team");
				return;
			}
			
			if (clicked.getScoreboard().getPlayerTeam(clicked) != null) {
				player.sendMessage(ChatColor.RED + "That player is already on a team.");
				return;
			}
			
			Team t = null;
			
			for (Team team : player.getScoreboard().getTeams()) {
				if (team.getSize() == 0) {
					t = team;
					break;
				}
			}
			
			if (t == null) {
				player.sendMessage(ChatColor.RED + "There are no teams with no people on.");
				return;
			}
			
			t.addPlayer(player);
			t.addPlayer(clicked);
			for (Player online : Bukkit.getServer().getOnlinePlayers()) {
				online.sendMessage(prefix() + ChatColor.GREEN + clicked.getName() + " §7and§a " + player.getName() + " §7has found each other and are now on a team.");
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lafs")) {
			if (sender.hasPermission("lafs.manage")) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Usage: /lafs <enable/disable>");
					return true;
				}
				
				if (args[0].equalsIgnoreCase("enable")) {
					if (enabled) {
						sender.sendMessage(prefix() + "LAFS is already enabled.");
						return true;
					}
					enabled = true;
					sender.sendMessage(prefix() + "LAFS has been enabled.");
				}
				else if (args[0].equalsIgnoreCase("disable")) {
					if (!enabled) {
						sender.sendMessage(prefix() + "LAFS is not enabled.");
						return true;
					}
					enabled = false;
					sender.sendMessage(prefix() + "LAFS has been disabled.");
				}
				else {
					sender.sendMessage(ChatColor.RED + "Usage: /lafs <enable/disable>");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have access to that command.");
			}
		}
		return true;
	}
}