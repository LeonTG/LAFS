/*
 * Project: LAFS
 * Class: com.leontg77.lafs.listeners.ClickListener
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Leon Vaktskjold <leontg77@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.leontg77.lafs.listeners;

import com.google.common.collect.Lists;
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

import java.util.*;

/**
 * Click listener class.
 *
 * @author LeonTG77
 */
public class ClickListener implements Listener {
    private final Scoreboard board;
    private final Main plugin;

    public ClickListener(Main plugin) {
        this.plugin = plugin;
        this.board = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    @EventHandler
    public void on(PlayerInteractEntityEvent event) {
        Entity rightClicked = event.getRightClicked();

        if (!(rightClicked instanceof Player)) {
            return;
        }

        Player clicked = (Player) rightClicked;
        Player player = event.getPlayer();

        int size = plugin.getSize();

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

    /**
     * Handle the click for solos.
     *
     * @param player The player clicking.
     * @param clicked The clicked player.
     */
    private void handleSolos(Player player, Player clicked) {
        if (board.getPlayerTeam(player) != null) {
            player.sendMessage(ChatColor.RED + "You are already on a team.");
            return;
        }

        if (board.getPlayerTeam(clicked) != null) {
            if (plugin.isAnonymous()) {
                player.sendMessage(ChatColor.RED + "That player is already on a team.");
            } else {
                player.sendMessage(ChatColor.RED + "'" + clicked.getName() + "' is already on a team.");
            }
            return;
        }

        Team teamToUse = null;

        List<Team> teams = Lists.newArrayList(board.getTeams());
        Collections.sort(teams, (o1, o2) -> o1.getName().compareTo(o2.getName()));

        for (Team team : teams) {
            if (team.getSize() == 0) {
                teamToUse = team;
                break;
            }
        }

        if (teamToUse == null) {
            player.sendMessage(ChatColor.RED + "There are no teams available.");
            return;
        }

        if (plugin.isAnonymous()) {
            plugin.broadcast(Main.PREFIX + "Two players have found each other and they are now on a team together!");
        } else {
            plugin.broadcast(Main.PREFIX + ChatColor.GREEN + player.getName() + "§7 has found §a" + clicked.getName() + " §7and they are now on a team together!");
        }

        teamToUse.addPlayer(player);
        teamToUse.addPlayer(clicked);
    }

    /**
     * Handle the click for solos.
     *
     * @param player The player clicking.
     * @param clicked The clicked player.
     */
    private void handleTeams(Player player, Player clicked) {
        if (board.getPlayerTeam(player) == null) {
            player.sendMessage(ChatColor.RED + "You are not on a team");
            return;
        }

        if (board.getPlayerTeam(clicked) == null) {
            if (plugin.isAnonymous()) {
                player.sendMessage(ChatColor.RED + "That player is not on a team.");
            } else {
                player.sendMessage(ChatColor.RED + "'" + clicked.getName() + "' is not on a team.");
            }
            return;
        }

        Team clickedTeam = board.getPlayerTeam(clicked);
        Team playerTeam = board.getPlayerTeam(player);

        if (playerTeam.getSize() != plugin.getSize()) {
            player.sendMessage(ChatColor.RED + "Your teamsize isn't the same as set in the config.");
            return;
        }

        if (clickedTeam.equals(playerTeam)) {
            if (plugin.isAnonymous()) {
                player.sendMessage(ChatColor.RED + "That player is already on your team.");
            } else {
                player.sendMessage(ChatColor.RED + "'" + clicked.getName() + "' is already on your team.");
            }
            return;
        }

        if (clickedTeam.getSize() != playerTeam.getSize()) {
            player.sendMessage(ChatColor.RED + "The teamsizes between your and their team does not match.");
            return;
        }

        if (plugin.isAnonymous()) {
            plugin.broadcast(Main.PREFIX + "Two teams have found each other and their teams have been combined!");
        } else {
            plugin.broadcast(Main.PREFIX + "Team §a" + teamString(playerTeam) + "§7 has found §a" + teamString(clickedTeam) + " §7and their teams have been combined!");
        }

        for (OfflinePlayer players : clickedTeam.getPlayers()) {
            players.getName(); // just so it wouldn't tell me to use .forEach because I want Java 7 compatibility.
            playerTeam.addPlayer(players);
        }
    }

    /**
     * Make a string version out of this team with it's name and players.
     *
     * @param team The team to use.
     * @return The string version.
     */
    private String teamString(Team team) {
        return team.getName() + team.getEntries().toString().replaceAll("\\[", "(").replace("]", ")");
    }
}