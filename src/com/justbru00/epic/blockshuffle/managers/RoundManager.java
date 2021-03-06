package com.justbru00.epic.blockshuffle.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.justbru00.epic.blockshuffle.beans.RoundPlayerInfo;
import com.justbru00.epic.blockshuffle.main.EpicBlockShuffle;
import com.justbru00.epic.blockshuffle.stats.StatsManager;
import com.justbru00.epic.blockshuffle.utils.Messager;

import io.netty.util.internal.ThreadLocalRandom;

public class RoundManager {
	private static int roundTaskId = -1;

	private static ArrayList<Material> randomMaterials = new ArrayList<Material>();

	private static HashMap<UUID, RoundPlayerInfo> playerInfoMap = new HashMap<UUID, RoundPlayerInfo>();

	private static BossBar countdownBossBar;
	private static int countdownCounter = 300;
	
	private static int roundCounter = 1;
	
	private static boolean roundHasBeenPlayedBefore = false;

	public static void loadRandomMaterials() {
		randomMaterials.clear();
		for (String s : EpicBlockShuffle.getInstance().getConfig().getStringList("randomblocks")) {
			randomMaterials.add(Material.getMaterial(s));
		}
	}	

	public static int getRoundTaskId() {
		return roundTaskId;
	}

	public static void resetAllPlayers() {
		playerInfoMap.clear();
	}

	public static void startClock() {	
		Messager.sendBC("&6Round " + roundCounter + " begins...");
		roundHasBeenPlayedBefore = true;
		Bukkit.dispatchCommand(EpicBlockShuffle.getConsole(), "recipe give @a *");
		Bukkit.getWorld("world").setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
		Bukkit.getWorld("world").setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
		Bukkit.getWorld("world").setGameRule(GameRule.DO_LIMITED_CRAFTING, false);
		Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);

		roundTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(EpicBlockShuffle.getInstance(), new Runnable() {

			@Override
			public void run() {
				
				// Add all online players to the alive team.
				if (countdownCounter == 0) {
					// TIMES UP

					ArrayList<UUID> toRemove = new ArrayList<UUID>();
					
					for (Entry<UUID, RoundPlayerInfo> entry : playerInfoMap.entrySet()) {
						// Stats logic
						StatsManager.singlePlayerEndOfRoundBlockStats(entry.getValue().getBlockToFind(), roundCounter, entry.getValue().isBlockFound(), entry.getKey());
						
						// Failed player logic
						if (!entry.getValue().isBlockFound()) {
							// FAILED ROUND
							OfflinePlayer offline = Bukkit.getOfflinePlayer(entry.getKey());

							if (offline.isOnline()) {
								final Player online = offline.getPlayer();
								online.setHealth(0.0);
								Bukkit.getScheduler().scheduleSyncDelayedTask(EpicBlockShuffle.getInstance(),
										new Runnable() {

											@Override
											public void run() {
												if (online.isOnline()) {
													online.setGameMode(GameMode.SPECTATOR);
												}
											}
										}, 20);
							}

							Messager.sendBC("&c" + offline.getName() + " failed to find their block.");		
							toRemove.add(entry.getKey());
							entry.getValue().getMaterialDisplayTeam().deleteFromServer();
						}
					}
					
					for (UUID id : toRemove) {
						playerInfoMap.remove(id);					
					}
					
					if (playerInfoMap.size() == 1) {
						// WINNER
						for (Entry<UUID, RoundPlayerInfo> entry : playerInfoMap.entrySet()) {
							Messager.sendTitleToAll("&aWINNER!",
									"&7" + Bukkit.getPlayer(entry.getKey()).getName() + " won the game!");
						}
						stopClock();
					} else if (playerInfoMap.size() == 0) {
						// ALL LOST
						Messager.sendTitleToAll("&cDRAW", "&cNo one won the game!");
						resetAllPlayers();
						stopClock();
					} else {
						// KEEP GOING
						for (Entry<UUID, RoundPlayerInfo> entry : playerInfoMap.entrySet()) {
							entry.getValue().setBlockToFind(randomMaterials
									.get(ThreadLocalRandom.current().nextInt(0, randomMaterials.size())));
							entry.getValue().setBlockFound(false);
							
						}
						roundCounter++;
						Messager.sendBC("&6Round " + roundCounter + " begins...");
						
						if (roundCounter >= 4 && roundCounter < 7) {
							// SHORTEN ROUND TIME
							countdownCounter = 240;
							Messager.sendTitleToAll("&r", "&7&oYou have 4 minutes to complete this round.");
						} else if (roundCounter >= 7) {
							// SHORTEN ROUND TIME MORE
							countdownCounter = 180;
							Messager.sendTitleToAll("&r", "&7&oYou have 3 minutes to complete this round.");
						} else {
							countdownCounter = 300;	
						}				
					}

				}

				// IF NOT CONTINUE COUNTDOWN
				// SHOW ACTION BAR WITH BLOCK
				for (Entry<UUID, RoundPlayerInfo> entry : playerInfoMap.entrySet()) {
					OfflinePlayer offline = Bukkit.getOfflinePlayer(entry.getKey());
					if (!entry.getValue().isBlockFound()) {
						Material material = entry.getValue().getBlockToFind();
						String name = material.name().toLowerCase().replace("_", " ");
						boolean vowel = ("aeiou".indexOf(name.charAt(0)) != -1);						
						if (offline.isOnline()) {
							Messager.sendActionBar(("&6Find and stand on " + (vowel ? "an " : "a ") + name + "."),
									Bukkit.getPlayer(entry.getKey()));
						}						
					} else {
						if (offline.isOnline()) {
							Messager.sendActionBar("&6Congrats! Your next block will be chosen next round.", Bukkit.getPlayer(entry.getKey()));
						}
					}
				}

				// WHEN 10 SECONDS REMAIN, SHOW BOSSBAR AND COUNTDOWN
				if (countdownCounter <= 10) {
					showBossBarCountdown(countdownCounter, 10.0);
				} else {
					hideBossBarCountdown();
				}

				if (countdownCounter > 0) {
					countdownCounter--;
				}
			}
		}, 20, 20);

	}

	public static HashMap<UUID, RoundPlayerInfo> getPlayerInfoMap() {
		return playerInfoMap;
	}

	public static int getCountdownCounter() {
		return countdownCounter;
	}
	
	

	public static void setCountdownCounter(int countdownCounter) {
		RoundManager.countdownCounter = countdownCounter;
	}

	public static void stopClock() {
		Bukkit.getScheduler().cancelTask(roundTaskId);
		countdownCounter = 300;
		hideBossBarCountdown();
		roundTaskId = -1;
		roundCounter = 1;
		for (Entry<UUID, RoundPlayerInfo> entry : playerInfoMap.entrySet()) {
			entry.getValue().getMaterialDisplayTeam().deleteFromServer();
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(EpicBlockShuffle.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				Bukkit.shutdown();				
			}
		}, 20*10);
	}

	public static void showBossBarCountdown(int timeLeft, double totalTime) {
		if (countdownBossBar == null) {
			countdownBossBar = Bukkit.createBossBar(Messager.color("&cTime Remaining:"), BarColor.RED,
					BarStyle.SEGMENTED_10);
		}

		countdownBossBar.removeAll();
		for (Player p : Bukkit.getOnlinePlayers()) {
			countdownBossBar.addPlayer(p);
			p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0F, 1.0F);
		}

		countdownBossBar.setVisible(true);
		countdownBossBar.setTitle(Messager.color("&cTime Remaining: " + timeLeft));
		countdownBossBar.setProgress(timeLeft / totalTime);
	}

	public static void hideBossBarCountdown() {
		if (countdownBossBar == null) {
			countdownBossBar = Bukkit.createBossBar(Messager.color("&cTime Remaining:"), BarColor.RED,
					BarStyle.SEGMENTED_10);
		}

		countdownBossBar.setVisible(false);
	}

	public static void playerMovedToNewBlock(Player p, Material block) {		
		RoundPlayerInfo rpi = playerInfoMap.get(p.getUniqueId());
		if (rpi == null) {
			return;
		}

		if (rpi.getBlockToFind().equals(block) && !rpi.isBlockFound()) {
			// FOUND THE BLOCK
			p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
			playDingToAllExcept(p);
			Messager.sendBC(p.getName() + " found their block.");
			rpi.setBlockFound(true);
		} else {
			// Do nothing
		}
	}

	private static void playDingToAllExcept(Player exception) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getUniqueId().equals(exception.getUniqueId())) {
				// Do nothing
			} else {
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			}
		}
	}

	public static void addPlayer(Player p) {
		RoundPlayerInfo rpi = new RoundPlayerInfo(p.getUniqueId());
		rpi.setBlockToFind(randomMaterials.get(ThreadLocalRandom.current().nextInt(0, randomMaterials.size())));
		rpi.setBlockFound(false);

		playerInfoMap.put(p.getUniqueId(), rpi);
	}
	
	public static boolean hasRoundBeenPlayedBefore() {
		return roundHasBeenPlayedBefore;
	}

}
