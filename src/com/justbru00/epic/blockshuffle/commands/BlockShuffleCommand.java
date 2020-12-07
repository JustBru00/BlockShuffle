package com.justbru00.epic.blockshuffle.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.justbru00.epic.blockshuffle.managers.RoundManager;
import com.justbru00.epic.blockshuffle.utils.Messager;

public class BlockShuffleCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("blockshuffle")) {
			if (sender.hasPermission("epicblockshuffle.blockshuffle")) {
				if (args.length == 0) {
					Messager.msgSender("&cPlease provide arguments after /blockshuffle <start,stop,timeleft,settime>", sender);
					return true;
				}

				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("start")) {
						if (RoundManager.getRoundTaskId() == -1) {
							for (Player p : Bukkit.getOnlinePlayers()) {
								if (p.getGameMode().equals(GameMode.SURVIVAL))
									p.addPotionEffect(
											new PotionEffect(PotionEffectType.REGENERATION, 30, 4, false, false));
								p.getInventory().clear();
								p.setFoodLevel(20);
								p.setSaturation(10.0F);
								p.getWorld().setTime(0);
								RoundManager.addPlayer(p);
							}

							RoundManager.startClock();
							Messager.msgSender("&aStarted game!", sender);
						} else {
							Messager.msgSender("&cGame already started.", sender);
						}
						return true;
					} else if (args[0].equalsIgnoreCase("stop")) {
						RoundManager.resetAllPlayers();
						RoundManager.stopClock();
						Messager.msgSender("&aStopped game!", sender);
						return true;
					} else if (args[0].equalsIgnoreCase("timeleft")) {
						Messager.msgSender(
								"&6The current round has " + RoundManager.getCountdownCounter() + " seconds remaining.",
								sender);
						return true;
					} else if (args[0].equalsIgnoreCase("settime")) {
						if (args.length == 2) {
							int newTime = -1;
							
							try {
								newTime = Integer.parseInt(args[1]);
							} catch (Exception e) {
								Messager.msgSender("&cPlease provide an integer as the second argument. /blockshuffle settime <timeInSeconds>", sender);
								return true;
							}
							
							if (newTime == -1) {
								Messager.msgSender("&cPlease provide an integer as the second argument. /blockshuffle settime <timeInSeconds>", sender);
								return true;
							}
							if (RoundManager.getRoundTaskId() != -1) {
								// INGAME
								RoundManager.setCountdownCounter(newTime);
							}
							Messager.msgSender("&aSet the time remaining in this round to " + newTime + ".", sender);
							return true;
							
						} else {
							Messager.msgSender("&cYou must provide the correct arguments. /blockshuffle settime <timeInSeconds>", sender);
							return true;
						}
					} else {
						Messager.msgSender(
								"&cPlease provide correct arguments after /blockshuffle <start,stop,timeleft>", sender);
						return true;
					}
				} else {
					Messager.msgSender("&cPlease provide correct arguments after /blockshuffle <start,stop,timeleft>",
							sender);
					return true;
				}
			} else {
				Messager.msgSender("&cSorry you don't have permission.", sender);
				return true;
			}
		}
		return false;
	}
}
