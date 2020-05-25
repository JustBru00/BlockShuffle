package com.justbru00.epic.blockshuffle.main;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.justbru00.epic.blockshuffle.commands.BlockShuffleCommand;
import com.justbru00.epic.blockshuffle.listeners.BlockShuffleListeners;
import com.justbru00.epic.blockshuffle.managers.GameAutoStartManager;
import com.justbru00.epic.blockshuffle.managers.RoundManager;
import com.justbru00.epic.blockshuffle.team.TeamManager;
import com.justbru00.epic.blockshuffle.utils.Messager;

public class EpicBlockShuffle extends JavaPlugin {

	private static EpicBlockShuffle instance;
	private static ConsoleCommandSender console = Bukkit.getConsoleSender();
	private static Logger logger = Bukkit.getLogger();
	private static String prefix = Messager.color("&8[&bEpic&fBlockShuffle&8] &6");
	
	@Override
	public void onDisable() {
		TeamManager.cleanupOnShutdown();
		instance = null;
	}

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		RoundManager.loadRandomMaterials();
		Bukkit.getPluginManager().registerEvents(new BlockShuffleListeners(), instance);
		getCommand("blockshuffle").setExecutor(new BlockShuffleCommand());
		
		Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
			
			@Override
			public void run() {
				GameAutoStartManager.everySecond();				
			}
		}, 0, 20);
		
		// FIXES ISSUE WITH TAB COLORS NOT BEING UPDATED CORRECTLY.
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
			
			@Override
			public void run() {
				if (RoundManager.getRoundTaskId() == -1) {
					TeamManager.update();
				}				
			}
		}, 5, 20*30);
		
		Messager.msgConsole("&aEnabled!");
	}

	public static EpicBlockShuffle getInstance() {
		return instance;
	}

	public static ConsoleCommandSender getConsole() {
		return console;
	}

	public static Logger getBukkitLogger() {
		return logger;
	}

	public static String getPrefix() {
		return prefix;
	}
	
	
}
