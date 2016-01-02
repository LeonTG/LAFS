package com.leontg77.lafs.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.leontg77.lafs.Main;
import com.leontg77.lafs.Utils;

/**
 * Click listener class.
 * 
 * @author LeonTG77
 */
@SuppressWarnings("deprecation")
public class ClickListener implements Listener {
	private Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
	
	@EventHandler
	public void on(PlayerInteractEntityEvent event) {
		Entity rightClicked = event.getRightClicked();
		
		if (!(rightClicked instanceof Player)) {
			return;
		}

		Player clicked = (Player) rightClicked;
		Player player = event.getPlayer();
		
		int size = Main.getInstance().getSize();
		
		if (size == 1) {
			handleSolos(player, clicked);
		} 
		else if (size > 1) {
			handleTeams(player, clicked);
		}
		else {
			player.sendMessage(ChatColor.RED + "The team size is below 1, please set it to 1 or higher with /lafs teamsize.");
		}
	}
	
	public void handleSolos(Player player, Player clicked) {
		if (board.getPlayerTeam(player) != null) {
			player.sendMessage(ChatColor.RED + "You are already on a team");
			return;
		}
		
		if (board.getPlayerTeam(clicked) != null) {
			player.sendMessage(ChatColor.RED + "That player is already on a team.");
			return;
		}
		
		Team teamToUse = null;
		
		for (Team team : player.getScoreboard().getTeams()) {
			if (team.getSize() == 0) {
				teamToUse = team;
				break;
			}
		}
		
		if (teamToUse == null) {
			player.sendMessage(ChatColor.RED + "There are no teams available.");
			return;
		}
		
		teamToUse.addPlayer(player);
		teamToUse.addPlayer(clicked);
		
		Utils.broadcast(Main.PREFIX + ChatColor.GREEN + clicked.getName() + " §7and§a " + player.getName() + " §7has found each other and are now on a team.");
	}
	
	public void handleTeams(Player player, Player clicked) {
		if (board.getPlayerTeam(player) == null) {
			player.sendMessage(ChatColor.RED + "You are not on a team");
			return;
		}
		
		if (board.getPlayerTeam(clicked) == null) {
			player.sendMessage(ChatColor.RED + "That player is not on a team.");
			return;
		}

		Team team1 = board.getPlayerTeam(clicked);
		Team team2 = board.getPlayerTeam(player);
		
		if (team1 == team2) {
			player.sendMessage(ChatColor.RED + "That player is already on your team.");
			return;
		}
		
		for (OfflinePlayer players : team1.getPlayers()) {
			team2.addPlayer(players);
		}

		Utils.broadcast(Main.PREFIX + "Team §a" + team1.getName() + " §7and§a " + team2.getName() + " §7has found each other and are now on a team together.");
	}
}