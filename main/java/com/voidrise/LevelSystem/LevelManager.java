package com.voidrise.LevelSystem;

import com.voidrise.RiseVoid;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelManager {

    private final HashMap<UUID, Integer> xpMap = new HashMap<>();
    private final HashMap<UUID, Integer> levelMap = new HashMap<>();

    private final SpaceLordManager spacelordManager;
    private final RiseVoid plugin;

    public LevelManager(RiseVoid plugin) {
        this.plugin = plugin;
        this.spacelordManager = new SpaceLordManager(this, plugin);
    }

    public void addXP(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int currentXP = xpMap.getOrDefault(uuid, 0);
        int currentLevel = levelMap.getOrDefault(uuid, 1);

        int newXP = currentXP + amount;
        int requiredXP = getXPForNextLevel(currentLevel);

        while (newXP >= requiredXP) {
            newXP -= requiredXP;
            currentLevel++;
            player.sendMessage("§bSeviye atladın! Yeni seviye: §e" + currentLevel);
            requiredXP = getXPForNextLevel(currentLevel);
        }

        xpMap.put(uuid, newXP);
        levelMap.put(uuid, currentLevel);

        spacelordManager.checkForNewSpacelord();
    }

    public int getXP(Player player) {
        return xpMap.getOrDefault(player.getUniqueId(), 0);
    }

    public int getLevel(Player player) {
        return levelMap.getOrDefault(player.getUniqueId(), 1);
    }

    public int getXPForNextLevel(int level) {
        return 50 + (level * 25);
    }

    public SpaceLordManager getSpacelordManager() {
        return spacelordManager;
    }

    public Map<UUID, Integer> getAllPlayerLevels() {
        return new HashMap<>(levelMap);
    }
}
