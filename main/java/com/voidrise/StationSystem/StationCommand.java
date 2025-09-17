package com.voidrise.StationSystem;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StationCommand implements CommandExecutor {

    private final StationManager stationManager;

    public StationCommand(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Bu komutu sadece oyuncular kullanabilir!");
            return true;
        }

        String prefix = "§6[§5VoidRise§6] §a";

        if (args.length == 1 && args[0].equalsIgnoreCase("go")) {
            Location stationLoc = stationManager.getPlayerStation(p);
            if (stationLoc == null) {
                Location newLoc = stationManager.createPlayerStation(p);
                p.teleport(newLoc.clone().add(0.5, 1, 0.5));
                p.sendMessage(prefix + "Modül Üssüne ilk kez ışınlandın, başarılar!");
            } else {
                p.teleport(stationLoc.clone().add(0.5, 1, 0.5));
                p.sendMessage(prefix + "Modül Üssüne ışınlandın.");
            }
            return true;
        }

        p.sendMessage(prefix + "Kullanım: /station go");
        return true;
    }
}
