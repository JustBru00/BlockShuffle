package com.justbru00.epic.blockshuffle.team;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;



/**
 *   This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * @author Justin Brubaker
 *
 */
public class EpicTeam {

	private ChatColor glowingColor = ChatColor.WHITE;
	private String teamName;
	private boolean friendlyFire = false;
	private ArrayList<String> players = new ArrayList<String>();
	private String prefix = "";
	private String suffix = "";
	
	public EpicTeam(String name, String _prefix) {
		prefix = _prefix;
		teamName = name;
	}		
	/**
	 * Updates the team on the server.
	 * @param full True if you need to update everything on the team. False if you just need players updated.
	 */
	public void update(boolean full) {
		Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
		Team theTeam;
		try { 
			theTeam = sb.registerNewTeam(teamName);		 
		} catch (IllegalArgumentException e) {
			// Nothing. Fails silently
		}
			
		theTeam = sb.getTeam(teamName);
			
		if (full) {
			theTeam.setAllowFriendlyFire(friendlyFire);
			theTeam.setPrefix(prefix);
			theTeam.setSuffix(suffix);
			theTeam.setColor(glowingColor);
			
			for (String s : theTeam.getEntries()) { // First remove all players from team
				theTeam.removeEntry(s);
			}
			
			for (String playerName : players) { // Next add all players back. This includes any new players.
				theTeam.addEntry(playerName);
			}
		} else {
			// Players only
			for (String s : theTeam.getEntries()) { // First remove all players from team
				theTeam.removeEntry(s);
			}
			
			for (String playerName : players) { // Next add all players back. This includes any new players.
				theTeam.addEntry(playerName);
			}
			
		}
	}
	
	public void deleteFromServer() {
		try {
			Team theTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
			theTeam.unregister();
		} catch (Exception e) {
			// fail silently
		}	
	}
	
	public void removePlayer(Player p) {
		removePlayerByName(p.getName());
	}
	
	public void addPlayer(Player p) {
		addPlayerByName(p.getName());
	}
	
	public void addPlayerByName(String playerName) {
		players.add(playerName);
	}
	
	public void removePlayerByName(String playerName) {
		players.remove(playerName);
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}
	/**
	 * Just removes all players from the ArrayList.
	 * You will need to use {@link #update(boolean)} to update teh actual scoreboard team.
	 */
	public void removeAllPlayers() {
		players.clear();
	}
	
	public ChatColor getGlowingColor() {
		return glowingColor;
	}
	
	public void setGlowingColor(ChatColor glowingColor) {
		this.glowingColor = glowingColor;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public boolean isFriendlyFire() {
		return friendlyFire;
	}

	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}
	
	
	
}
