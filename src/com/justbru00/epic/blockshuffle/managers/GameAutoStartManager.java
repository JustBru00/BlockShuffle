package com.justbru00.epic.blockshuffle.managers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.justbru00.epic.blockshuffle.utils.Messager;

public class GameAutoStartManager {

	private static int countdown = -1;
	private static BossBar countdownBossBar;

	public static void everySecond() {
		
		if (countdown >= 0) {
			if (RoundManager.getRoundTaskId() == -1) {
				showBossBarCountdown(countdown, 90);
			}
		}

		if (countdown == 0) {
			// GAME STARTS NOW
			if (RoundManager.getRoundTaskId() == -1) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getGameMode().equals(GameMode.SURVIVAL))
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * 20, 4, false, false));
					p.getInventory().clear();
					p.setFoodLevel(20);
					p.setSaturation(10.0F);
					p.getWorld().setTime(0);
					RoundManager.addPlayer(p);
					hideBossBarCountdown();
				}

				RoundManager.startClock();
				Messager.msgConsole("&aStarted game!");
			} else {
				Messager.msgConsole("&cGame already started.");
			}
		}

		if (countdown == -1) {
			// WAITING FOR PLAYERS
			if (RoundManager.getRoundTaskId() == -1  && !RoundManager.hasRoundBeenPlayedBefore()) {
				showBossBarWaitingMsg();
			}
		}

		if (countdown > -1) {
			countdown--;
		}		
	}
	
	public static int getCountDownTimer() {
		return countdown;
	}

	public static void startCountToGame() {
		countdown = 90;
	}

	public static void cancelCountToGame() {
		countdown = -1;
	}

	public static void showBossBarWaitingMsg() {
		if (countdownBossBar == null) {
			countdownBossBar = Bukkit.createBossBar(Messager.color("&aWaiting for players..."), BarColor.GREEN,
					BarStyle.SOLID);
		}

		countdownBossBar.removeAll();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			countdownBossBar.addPlayer(p);
		}

		countdownBossBar.setVisible(true);
		countdownBossBar.setTitle(Messager.color("&aWaiting for players..."));
		countdownBossBar.setProgress(1.0F);
	}

	public static void showBossBarCountdown(int timeLeft, double totalTime) {
		if (countdownBossBar == null) {
			countdownBossBar = Bukkit.createBossBar(Messager.color("&aGame Starts in: "), BarColor.GREEN,
					BarStyle.SOLID);
		}

		countdownBossBar.removeAll();

		for (Player p : Bukkit.getOnlinePlayers()) {
			countdownBossBar.addPlayer(p);
			if (timeLeft <= 10) {
				p.playSound(p.getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1.0F, 1.0F);
			}
		}

		countdownBossBar.setVisible(true);
		countdownBossBar.setTitle(Messager.color("&aGame Starts in: " + timeLeft));
		countdownBossBar.setProgress(timeLeft / totalTime);
	}

	public static void hideBossBarCountdown() {
		if (countdownBossBar == null) {
			countdownBossBar = Bukkit.createBossBar(Messager.color("&aWaiting for players..."), BarColor.GREEN,
					BarStyle.SOLID);
		}

		countdownBossBar.setVisible(false);
	}
}
