package ru.ancap.framework.api.material.materials;

import org.bukkit.Material;

import java.util.List;

public interface Flower {

    List<Material> flowers = List.of(
            Material.DANDELION,
            Material.POPPY,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.RED_TULIP,
            Material.ORANGE_TULIP,
            Material.WHITE_TULIP,
            Material.OXEYE_DAISY,
            Material.PINK_TULIP,
            Material.CORNFLOWER,
            Material.LILY_OF_THE_VALLEY,
            Material.WITHER_ROSE,
            Material.SUNFLOWER,
            Material.ROSE_BUSH,
            Material.LILAC,
            Material.PEONY
    );

    static boolean isFlower(Material material) {
        for (Material flower : flowers) {
            if (material == flower) return true;
        }
        return false;
    }

}
