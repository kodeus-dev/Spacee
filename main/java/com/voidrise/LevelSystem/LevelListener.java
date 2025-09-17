package com.voidrise.LevelSystem;

import com.voidrise.LevelSystem.LevelManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Set;

public class LevelListener implements Listener {

    private final LevelManager levelManager;

    private final Set<Material> xpBlocks = Set.of(
            Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
            Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
            Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE,
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
            Material.NETHERITE_BLOCK, Material.ANCIENT_DEBRIS
    );

    public LevelListener(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled() && xpBlocks.contains(event.getBlock().getType())) {
            Player player = event.getPlayer();
            levelManager.addXP(player, 5);
        }
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            levelManager.addXP(killer, 10);
        }
    }
}
