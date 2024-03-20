package ru.ancap.framework.util.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationKey {
    
    public static String get(Location location) {
        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }
    
    public static Location ofKey(String key) {
        String[] data = key.split(";");
        return new Location(
            Bukkit.getWorld(data[0]),
            Double.parseDouble(data[1]),
            Double.parseDouble(data[2]),
            Double.parseDouble(data[3])
        );
    }
    
}