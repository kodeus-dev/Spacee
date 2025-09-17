package com.voidrise;

import com.voidrise.LevelSystem.LevelListener;
import com.voidrise.LevelSystem.LevelManager;
import com.voidrise.LevelSystem.LevelPlaceholder;
import com.voidrise.LevelSystem.SpaceLordManager;
import com.voidrise.StationSystem.StationCommand;
import com.voidrise.StationSystem.StationListener;
import com.voidrise.StationSystem.StationManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RiseVoid extends JavaPlugin {

    private LevelManager levelManager;

    @Override
    public void onEnable() {
        StationManager stationManager = new StationManager(this);
        getServer().getPluginManager().registerEvents(new StationListener(stationManager), this);
        getCommand("station").setExecutor(new StationCommand(stationManager));

        levelManager = new LevelManager(this);
        getServer().getPluginManager().registerEvents(new LevelListener(levelManager), this);
        new LevelPlaceholder(levelManager).register();

        levelManager.getSpacelordManager().checkForNewSpacelord();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            levelManager.getSpacelordManager().checkForNewSpacelord();
        }, 20L * 5);

        getLogger().info("RiseVoid Plugin aktif!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RiseVoid Plugin kapandı.");
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }
}
