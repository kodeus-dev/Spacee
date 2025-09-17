package com.voidrise.LevelSystem;

import com.voidrise.RiseVoid;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class SpaceLordManager {

    private final LevelManager levelManager;
    private final RiseVoid plugin;

    private UUID currentSpaceLordUUID = null;
    private int currentMaxLevel = 0;

    private static final String SPACELORD_GROUP = "spacelord";
    private static final String DEFAULT_GROUP = "default";

    public SpaceLordManager(LevelManager levelManager, RiseVoid plugin) {
        this.levelManager = levelManager;
        this.plugin = plugin;
    }

    public void checkForNewSpacelord() {
        Map<UUID, Integer> allLevels = levelManager.getAllPlayerLevels();

        int maxLevel = allLevels.values().stream().max(Integer::compareTo).orElse(0);
        long countMaxLevelPlayers = allLevels.values().stream().filter(lvl -> lvl == maxLevel).count();

        if (maxLevel == 0 || countMaxLevelPlayers != 1) {
            // spacelord esitlik
            if (currentSpaceLordUUID != null) {
                Player oldSL = Bukkit.getPlayer(currentSpaceLordUUID);
                if (oldSL != null) {
                    removeGroup(oldSL);
                    oldSL.sendMessage("§cSpacelord unvanını kaybettin çünkü geçerli bir spacelord yok.");
                }
                currentSpaceLordUUID = null;
                currentMaxLevel = 0;
            }
            return;
        }

        UUID newSpaceLordUUID = allLevels.entrySet().stream()
                .filter(e -> e.getValue() == maxLevel)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (newSpaceLordUUID == null) return;

        if (!newSpaceLordUUID.equals(currentSpaceLordUUID)) {
            if (maxLevel >= currentMaxLevel + 1) {
                // Eski spacelord sg
                if (currentSpaceLordUUID != null) {
                    Player oldSL = Bukkit.getPlayer(currentSpaceLordUUID);
                    if (oldSL != null) {
                        removeGroup(oldSL);
                        oldSL.sendMessage("§cSpacelord unvanını kaybettin.");
                    }
                }

                // Yeni spacelordu ekle
                Player newSL = Bukkit.getPlayer(newSpaceLordUUID);
                if (newSL != null) {
                    addGroup(newSL);
                    newSL.sendMessage("§aTebrikler! Yeni Spacelord sensin!");
                }

                currentSpaceLordUUID = newSpaceLordUUID;
                currentMaxLevel = maxLevel;
            }
        }
    }

    private void addGroup(Player player) {
        String cmd = "lp user " + player.getName() + " parent set " + SPACELORD_GROUP;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    private void removeGroup(Player player) {
        String cmd = "lp user " + player.getName() + " parent set " + DEFAULT_GROUP;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
}
