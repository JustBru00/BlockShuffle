package com.justbru00.epic.blockshuffle.stats;

import java.util.UUID;

import org.bukkit.Material;
import com.justbru00.epic.blockshuffle.main.EpicBlockShuffle;
import com.justbru00.epic.blockshuffle.utils.PluginFile;

public class StatsManager {

	public static void singlePlayerEndOfRoundBlockStats(Material blockToFind, int roundNumber, boolean found, UUID uuid) {
		PluginFile stats = EpicBlockShuffle.getStatsFile();
		
		// Global difficulty stats
		int materialFoundAmount = stats.getInt("difficulty.round_" + roundNumber + "." + blockToFind.toString() + ".found");
		int materialFailedAmount = stats.getInt("difficulty.round_" + roundNumber + "." + blockToFind.toString() + ".failed");
		
		if (found) {
			materialFoundAmount++;
		} else {
			materialFailedAmount++;
		}
		
		stats.set("difficulty.round_" + roundNumber + "." + blockToFind.toString() + ".found", materialFoundAmount);
		stats.set("difficulty.round_" + roundNumber + "." + blockToFind.toString() + ".failed", materialFailedAmount);
		
		// Player skill stats
		int playerFoundAmount = stats.getInt(uuid.toString() + "." + blockToFind.toString() + ".found");
		int playerFailedAmount = stats.getInt(uuid.toString() + "." + blockToFind.toString() + ".failed");
		
		if (found) {
			playerFoundAmount++;
		} else {
			playerFailedAmount++;
		}
		
		stats.set(uuid.toString() + "." + blockToFind.toString() + ".found", playerFoundAmount);
		stats.set(uuid.toString() + "." + blockToFind.toString() + ".failed", playerFailedAmount);
		
		stats.save();
	}

}
