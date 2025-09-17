package com.voidrise.StationSystem;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;


import com.voidrise.RiseVoid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.UUID;

public class StationManager {

    String prefix = "§6[§5VoidRise§6] §a";
    String prefixhata = "§6[§5VoidRise§6] §4";
    private final RiseVoid plugin;
    private Clipboard clipboard;
    private final HashMap<UUID, Location> playerStations = new HashMap<>();
    private int nextX = 0;

    public StationManager(RiseVoid plugin) {
        this.plugin = plugin;
        loadSchematic();
    }

    private void loadSchematic() {
        try {
            File schematicFile = new File("plugins/WorldEdit/schematics/modul.schem");
            if (!schematicFile.exists()) {
                plugin.getLogger().warning("modul.schem WorldEdit klasöründe bulunamadı!");
                return;
            }

            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
                clipboard = reader.read();
                plugin.getLogger().info("modul.schem başarıyla yüklendi (WorldEdit klasöründen).");
            }
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("modul.schem yüklenirken hata oluştu!");
        }
    }

    public Location getPlayerStation(Player player) {
        return playerStations.get(player.getUniqueId());
    }

    public Location createPlayerStation(Player player) {
        if (clipboard == null) {
            player.sendMessage(prefixhata + "§cModül yüklenemedi. Lütfen yöneticilere bildirin.");
            return player.getLocation();
        }

        Location baseLocation = new Location(
                Bukkit.getWorld("station_world"),
                nextX,
                100,
                0
        );

        nextX += 100;

        pasteSchematic(baseLocation);
        playerStations.put(player.getUniqueId(), baseLocation);
        return baseLocation;
    }

    private void pasteSchematic(Location loc) {
        try {
            if (clipboard == null) {
                plugin.getLogger().warning("Clipboard boş, schematic yüklenemedi.");
                return;
            }

            World weWorld = BukkitAdapter.adapt(loc.getWorld());
            EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld);

            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
                    .ignoreAirBlocks(true)
                    .build();

            Operations.complete(operation);
            editSession.close();

        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("Schematic yapıştırılırken hata oluştu!");
        }
    }

    public boolean isInStationArea(Location checkLoc, Location baseLoc) {
        if (!checkLoc.getWorld().equals(baseLoc.getWorld())) return false;

        int dx = Math.abs(checkLoc.getBlockX() - baseLoc.getBlockX());
        int dy = Math.abs(checkLoc.getBlockY() - baseLoc.getBlockY());
        int dz = Math.abs(checkLoc.getBlockZ() - baseLoc.getBlockZ());

        return dx <= 20 && dy <= 20 && dz <= 20;
    }
}
