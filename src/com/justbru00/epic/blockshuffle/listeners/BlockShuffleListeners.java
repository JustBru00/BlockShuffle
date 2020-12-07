package com.justbru00.epic.blockshuffle.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.justbru00.epic.blockshuffle.main.EpicBlockShuffle;
import com.justbru00.epic.blockshuffle.managers.GameAutoStartManager;
import com.justbru00.epic.blockshuffle.managers.RoundManager;

public class BlockShuffleListeners implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (RoundManager.getRoundTaskId() == -1) {
			// GAME NOT IN PROGRESS
			if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				Location from = e.getFrom();
				Location to = e.getTo();
				
				if (from.getX() != to.getX()) {
					e.setCancelled(true);
				} else if (from.getY() != to.getY()) {
					e.setCancelled(true);
				} else if (from.getZ() != to.getZ()) {
					e.setCancelled(true);
				}
			}
		}
		
		
		if (e.getFrom().getBlock().equals(e.getTo().getBlock())) {
			return;
		} else {
			RoundManager.playerMovedToNewBlock(e.getPlayer(), e.getTo().getBlock().getRelative(BlockFace.DOWN).getType());
		}		
	}
	
	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent e) {
		if (RoundManager.getRoundTaskId() == -1) {
			if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (RoundManager.getRoundTaskId() != -1 && !RoundManager.getPlayerInfoMap().containsKey(e.getPlayer().getUniqueId())) {
			e.getPlayer().setGameMode(GameMode.SPECTATOR);
		}
		
		if (RoundManager.getRoundTaskId() == -1) {
			// START AUTO COUNTDOWN
			if (Bukkit.getOnlinePlayers().size() >= 2 && GameAutoStartManager.getCountDownTimer() == -1) {
				GameAutoStartManager.startCountToGame();
			}
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(EpicBlockShuffle.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if (RoundManager.getRoundTaskId() == -1) {
					// NOT INGAME
					if (Bukkit.getOnlinePlayers().size() < 2) {
						// Cancel start.
						GameAutoStartManager.cancelCountToGame();
					}
				}				
			}
		}, 20);
	}
	
}
