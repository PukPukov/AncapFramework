package ru.ancap.util.location;

import org.bukkit.Location;

public class BlockLocation {
    
    public static boolean equals(Location one, Location other) {
        return one.getWorld().getName().equals(other.getWorld().getName()) &&
                one.getBlockX() == other.getBlockX() &&
                one.getBlockY() == one.getBlockY() &&
                one.getBlockZ() == other.getBlockZ();
    }
    
}
