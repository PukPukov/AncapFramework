package ru.ancap.framework.api.material.materials;

import org.bukkit.TreeType;

import static org.bukkit.Material.CRIMSON_HYPHAE;

public interface Wood {

    static Type typeOf(TreeType type) throws NotAWoodException {
        return switch (type) {
            case TREE, BIG_TREE, SWAMP -> Type.OAK;
            case REDWOOD, TALL_REDWOOD, MEGA_REDWOOD -> Type.SPRUCE;
            case ACACIA -> Type.ACACIA;
            case BIRCH, TALL_BIRCH -> Type.BIRCH;
            case SMALL_JUNGLE, JUNGLE, COCOA_TREE, JUNGLE_BUSH -> Type.JUNGLE;
            case DARK_OAK -> Type.DARK_OAK;
            case MANGROVE, TALL_MANGROVE -> Type.MANGROVE;
            default -> throw new NotAWoodException();
        };
    }

    enum Type {
        OAK,
        SPRUCE,
        ACACIA,
        JUNGLE,
        BIRCH,
        DARK_OAK,
        MANGROVE
    }

    class NotAWoodException extends Exception {
        
    }
}
