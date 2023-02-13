package ru.ancap.framework.api.material.materials;

import org.bukkit.Material;

import java.util.List;

public interface Ore {

    List<Material> ores = List.of(
            Material.COAL_ORE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.COPPER_ORE,
            Material.DIAMOND_ORE,
            Material.LAPIS_ORE,
            Material.REDSTONE_ORE,
            Material.EMERALD_ORE,
            Material.ANCIENT_DEBRIS,
            Material.DEEPSLATE_COAL_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.DEEPSLATE_REDSTONE_ORE
    );

    static boolean isOre(Material material) {
        for (Material ore : ores) {
            if (material == ore) return true;
        }
        return false;
    }

}
