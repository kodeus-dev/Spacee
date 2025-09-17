package com.voidrise.LevelSystem;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LevelPlaceholder extends PlaceholderExpansion {

    private final LevelManager levelManager;

    public LevelPlaceholder(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "risevoid";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "rzgr";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "1.0";
    }

    @Nullable
    @Override
    public String onRequest(@Nullable OfflinePlayer player, @NotNull String identifier) {
        if (player == null || !player.isOnline()) {
            return "";
        }

        Player onlinePlayer = player.getPlayer();
        if (onlinePlayer == null) return "";

        switch (identifier.toLowerCase()) {
            case "level":
                return String.valueOf(levelManager.getLevel(onlinePlayer));
            case "xp":
                return String.valueOf(levelManager.getXP(onlinePlayer));
            default:
                return null;
        }
    }
}
