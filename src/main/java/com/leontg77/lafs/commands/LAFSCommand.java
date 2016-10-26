/*
 * Project: LAFS
 * Class: com.leontg77.lafs.commands.LAFSCommand
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

package com.leontg77.lafs.commands;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;

import com.leontg77.lafs.Main;
import com.leontg77.lafs.listeners.ClickListener;
import org.bukkit.util.StringUtil;

import java.util.List;

/**
 * LAFS command class.
 *
 * @author LeonTG77
 */
public class LAFSCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "lafs.manage";

    private final ClickListener listener;
    private final Main plugin;

    public LAFSCommand(Main plugin, ClickListener listener) {
        this.listener = listener;
        this.plugin = plugin;
    }

    private boolean enabled = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.PREFIX + "Usage: /lafs <enable|disable|teamsize|anonymous> [teamsize]");
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(Main.PREFIX + "Plugin creator: §aLeonTG77");
            sender.sendMessage(Main.PREFIX + "Version: §a" + plugin.getDescription().getVersion());
            sender.sendMessage(Main.PREFIX + "Description:");
            sender.sendMessage("§8» §f" + plugin.getDescription().getDescription());
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (enabled) {
                sender.sendMessage(Main.PREFIX + "LAFS is already enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "LAFS has been enabled.");

            Bukkit.getPluginManager().registerEvents(listener, plugin);
            enabled = true;
            return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (!enabled) {
                sender.sendMessage(Main.PREFIX + "LAFS is not enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "LAFS has been disabled.");

            HandlerList.unregisterAll(listener);
            enabled = false;
            return true;
        }

        if (args[0].equalsIgnoreCase("teamsize")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (!enabled) {
                sender.sendMessage(Main.PREFIX + "LAFS is not enabled.");
                return true;
            }

            if (args.length == 1) {
                sender.sendMessage(Main.PREFIX + "Usage: /lafs teamsize <teamsize>");
                return true;
            }

            int size;

            try {
                size = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a valid teamsize.");
                return true;
            }

            if (size < 1) {
                sender.sendMessage(ChatColor.RED + "The combine teamsize can't be lower than 1.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "LAFS will now make is now §aTo" + size + "'s§7 into §aTo" + (size * 2) + "'s.");
            plugin.setSize(size);
            return true;
        }

        if (args[0].equalsIgnoreCase("anonymous")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (!enabled) {
                sender.sendMessage(Main.PREFIX + "LAFS is not enabled.");
                return true;
            }

            if (plugin.isAnonymous()) {
                sender.sendMessage(Main.PREFIX + "LAFS will now display who got on a team.");
                plugin.setAnonymous(false);
            } else {
                sender.sendMessage(Main.PREFIX + "LAFS will no longer display who got on a team.");
                plugin.setAnonymous(true);
            }
            return true;
        }

        sender.sendMessage(Main.PREFIX + "Usage: /lafs <enable|disable|teamsize|anonymous> [teamsize]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            return Lists.newArrayList();
        }

        List<String> list = Lists.newArrayList();

        list.add("info");

        if (sender.hasPermission(PERMISSION)) {
            list.add("enable");
            list.add("disable");
            list.add("teamsize");
            list.add("anonymous");
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], list, Lists.newArrayList());
    }
}