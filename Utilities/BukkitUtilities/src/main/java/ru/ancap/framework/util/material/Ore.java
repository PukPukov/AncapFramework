package ru.ancap.framework.util.material;

import org.bukkit.Material;

import java.util.Set;

public interface Ore {

    Set<String> oreIds = Set.of(
        "COAL_ORE",
        "IRON_ORE",
        "GOLD_ORE",
        "COPPER_ORE",
        "DIAMOND_ORE",
        "LAPIS_ORE",
        "REDSTONE_ORE",
        "EMERALD_ORE",
        "ANCIENT_DEBRIS",
        "DEEPSLATE_COAL_ORE",
        "DEEPSLATE_COPPER_ORE",
        "DEEPSLATE_DIAMOND_ORE",
        "DEEPSLATE_EMERALD_ORE",
        "DEEPSLATE_GOLD_ORE",
        "DEEPSLATE_IRON_ORE",
        "DEEPSLATE_LAPIS_ORE",
        "DEEPSLATE_REDSTONE_ORE"
    );

    static boolean isOre(Material material) {
        return oreIds.contains(material.name());
    }

}