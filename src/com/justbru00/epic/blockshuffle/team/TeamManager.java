package com.justbru00.epic.blockshuffle.team;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 *   This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * @author Justin Brubaker
 *
 */
public class TeamManager {

	private static EpicTeam finishedPlayers = new EpicTeam("finished", ChatColor.GREEN);
	private static EpicTeam unfinishedPlayers = new EpicTeam("not_finished", ChatColor.RED);
	
	/**
	 * Call when the game is in startup
	 */
	public static void roundStarting() {
		reset();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.getGameMode().equals(GameMode.SPECTATOR)) {
				unfinishedPlayers.addPlayer(p);
			}
		}
		finishedPlayers.update(true);
		unfinishedPlayers.update(true);
	}
	
	public static int getAmountFinished() {
		return finishedPlayers.getPlayers().size();
	}
	/**
	 * Updates the teams just because.
	 */
	public static void update() {
		finishedPlayers.update(true);
		unfinishedPlayers.update(true);
	}
	
	public static void reset() {
		ArrayList<String> temp = finishedPlayers.getPlayers();
		temp.clear();
		finishedPlayers.setPlayers(temp);
		finishedPlayers.update(true);
		
		ArrayList<String> tempTwo = unfinishedPlayers.getPlayers();
		tempTwo.clear();
		unfinishedPlayers.setPlayers(tempTwo);
		unfinishedPlayers.update(true);
	}
	
	public static void playerCompleted(String playerName) {
		unfinishedPlayers.removePlayerByName(playerName);
		unfinishedPlayers.update(true);
		
		finishedPlayers.addPlayerByName(playerName);
		finishedPlayers.update(true);
	}
	
	public static void cleanupOnShutdown() {
		finishedPlayers.deleteFromServer();
		unfinishedPlayers.deleteFromServer();
	}
	
}
