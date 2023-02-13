package ru.ancap.framework.api.material.materials;

import org.bukkit.Material;

import java.util.List;

public interface Log {

    List<Material> logs = List.of(
            Material.OAK_LOG,
            Material.DARK_OAK_LOG,
            Material.SPRUCE_LOG,
            Material.JUNGLE_LOG,
            Material.ACACIA_LOG,
            Material.MANGROVE_LOG,
            Material.BIRCH_LOG
    );

    static boolean isLog(Material material) {
        for (Material log : logs) {
            if (material == log) return true;
        }
        return false;
    }

}
