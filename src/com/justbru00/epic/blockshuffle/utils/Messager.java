package com.justbru00.epic.blockshuffle.utils;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.justbru00.epic.blockshuffle.main.EpicBlockShuffle;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 *   This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * @author Justin Brubaker
 *
 */
public class Messager {

	public static void sendActionBar(String msg, Player player) {
		msg = Messager.color(msg);
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
	}
	
	public static String color(String uncolored){			
		return ChatColor.translateAlternateColorCodes('&', uncolored);		
	}
	
	public static void msgConsole(String msg) {		
		if (EpicBlockShuffle.getConsole() != null) {
		EpicBlockShuffle.getConsole().sendMessage(EpicBlockShuffle.getPrefix() + Messager.color(msg));		
		} else {
			EpicBlockShuffle.getBukkitLogger().info(ChatColor.stripColor(Messager.color(msg)));
		}
	}
	
	public static void sendTitle(String title, String subtitle, Player p) {
		p.sendTitle(Messager.color(title), Messager.color(subtitle), 10, 4*20, 10);
	}
	
	/**
	 * Send a title to all players on the server
	 * @param title
	 * @param subtitle
	 */
	public static void sendTitleToAll(String title, String subtitle) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendTitle(Messager.color(title), Messager.color(subtitle), 10, 4*20, 10);
		}
	}
	
	public static void msgPlayer(String msg, Player player) {
		player.sendMessage(EpicBlockShuffle.getPrefix() + Messager.color(msg));		
	}	
	
	public static void msgSender(String msg, CommandSender sender) {
		//msg = msg.replace("{char}", Integer.toString(CharLimit.getCharLimit()));
		sender.sendMessage(EpicBlockShuffle.getPrefix() + Messager.color(msg));
	}	
	
	public static void sendBC(String msg) {
		Bukkit.broadcastMessage(EpicBlockShuffle.getPrefix() + Messager.color(msg));
	}
	
}
