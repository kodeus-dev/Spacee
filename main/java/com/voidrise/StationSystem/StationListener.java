package com.voidrise.StationSystem;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.entity.Player;

public class StationListener implements Listener {

    private final StationManager stationManager;

    public StationListener(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location stationLoc = stationManager.getPlayerStation(player);

        if (stationLoc != null && stationManager.isInStationArea(event.getBlock().getLocation(), stationLoc)) {
            event.setCancelled(true);
            player.sendMessage("§cÜssünde blok kıramazsın!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location stationLoc = stationManager.getPlayerStation(player);

        if (stationLoc != null && stationManager.isInStationArea(event.getBlock().getLocation(), stationLoc)) {
            event.setCancelled(true);
            player.sendMessage("§cÜssünde blok yerleştiremezsin!");
        }
    }
}
