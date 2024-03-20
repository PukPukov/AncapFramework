package ru.ancap.framework.util.material;

import org.bukkit.Material;

import java.util.Set;

public interface Log {

    Set<String> logIds = Set.of(
         "OAK_LOG",
         "DARK_OAK_LOG",
         "SPRUCE_LOG",
         "JUNGLE_LOG",
         "ACACIA_LOG",
         "MANGROVE_LOG",
         "BIRCH_LOG"
    );

    static boolean isLog(Material material) {
        return logIds.contains(material.name());
    }

}