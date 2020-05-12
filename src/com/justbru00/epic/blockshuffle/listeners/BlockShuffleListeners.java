package com.justbru00.epic.blockshuffle.listeners;

import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.justbru00.epic.blockshuffle.managers.RoundManager;

public class BlockShuffleListeners implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getFrom().getBlock().equals(e.getTo().getBlock())) {
			return;
		}
		
		RoundManager.playerMovedToNewBlock(e.getPlayer(), e.getTo().getBlock().getRelative(BlockFace.DOWN).getType());
	}
	
}
