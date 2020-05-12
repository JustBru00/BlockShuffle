package com.justbru00.epic.blockshuffle.beans;

import org.bukkit.Material;

public class RoundPlayerInfo {

	private Material blockToFind;
	private boolean blockFound = false;
	
	public Material getBlockToFind() {
		return blockToFind;
	}
	public void setBlockToFind(Material blockToFind) {
		this.blockToFind = blockToFind;
	}
	public boolean isBlockFound() {
		return blockFound;
	}
	public void setBlockFound(boolean foundBlock) {
		this.blockFound = foundBlock;
	}	
}
