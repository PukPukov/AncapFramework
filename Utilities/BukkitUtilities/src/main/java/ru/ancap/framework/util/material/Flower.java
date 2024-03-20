package ru.ancap.framework.util.material;

import org.bukkit.Material;

import java.util.Set;

public interface Flower {
    
    Set<String> flowerIds = Set.of(
        "DANDELION",
        "POPPY",
        "BLUE_ORCHID",
        "ALLIUM",
        "AZURE_BLUET",
        "RED_TULIP",
        "ORANGE_TULIP",
        "WHITE_TULIP",
        "OXEYE_DAISY",
        "PINK_TULIP",
        "CORNFLOWER",
        "LILY_OF_THE_VALLEY",
        "WITHER_ROSE",
        "SUNFLOWER",
        "ROSE_BUSH",
        "LILAC",
        "PEONY"
    );

    static boolean isFlower(Material material) {
        return flowerIds.contains(material.name());
    }

}