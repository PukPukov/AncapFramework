package ru.ancap.framework.util.location;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;

@UtilityClass
public class BlockLocation {
    
    public static boolean equals(Location one, Location other) {
        return one.getWorld().getName().equals(other.getWorld().getName()) &&
                one.getBlockX() == other.getBlockX() &&
                one.getBlockY() == other.getBlockY() &&
                one.getBlockZ() == other.getBlockZ();
    }
    
}
