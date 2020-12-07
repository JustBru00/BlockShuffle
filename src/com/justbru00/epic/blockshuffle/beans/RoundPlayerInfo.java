package com.justbru00.epic.blockshuffle.beans;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.justbru00.epic.blockshuffle.team.EpicTeam;

public class RoundPlayerInfo {

	private UUID playerUuid;
	private Material blockToFind;
	private boolean blockFound = false;
	private EpicTeam materialDisplayTeam;
	
	public RoundPlayerInfo(UUID _playerUuid) {
		playerUuid = _playerUuid;
	}

	public EpicTeam getMaterialDisplayTeam() {
		return materialDisplayTeam;
	}

	public void updateMaterialDisplayTeam() {
		if (materialDisplayTeam == null) {
			materialDisplayTeam = new EpicTeam("bs_" + String.valueOf(ThreadLocalRandom.current().nextInt(0, 2000000)), "");
		}

		if (blockToFind != null) {
			String name = blockToFind.name();
			StringBuilder sb = new StringBuilder();
			for (String oneString : name.toLowerCase().split("_")) {
				sb.append(oneString.substring(0, 1).toUpperCase());
				sb.append(oneString.substring(1) + " ");
			}
			
			if (blockFound) {
				materialDisplayTeam.setSuffix(" " + ChatColor.GREEN + "" + sb.toString().trim());
			} else {
				materialDisplayTeam.setSuffix(" " + ChatColor.RED + "" + sb.toString().trim());
			}
		}
		
		materialDisplayTeam.removeAllPlayers();
		materialDisplayTeam.addPlayerByName(Bukkit.getOfflinePlayer(playerUuid).getName());
		materialDisplayTeam.update(true);		
	}

	public Material getBlockToFind() {
		return blockToFind;
	}

	public void setBlockToFind(Material blockToFind) {
		this.blockToFind = blockToFind;
		updateMaterialDisplayTeam();
	}

	public boolean isBlockFound() {
		return blockFound;
	}

	public void setBlockFound(boolean foundBlock) {
		this.blockFound = foundBlock;
		updateMaterialDisplayTeam();
	}

	public UUID getPlayerUuid() {
		return playerUuid;
	}
	
}
