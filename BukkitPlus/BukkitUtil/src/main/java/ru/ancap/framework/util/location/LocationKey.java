package ru.ancap.framework.util.location;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@UtilityClass
public class LocationKey {
    
    public static String get(Location location) {
        String coords = location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ();
        return location.getWorld() != null ? location.getWorld().getName()+";"+coords : coords;
    }
    
    public static Location ofKey(String key) {
        String[] data = key.split(";");
        if (data.length == 3) return new org.bukkit.Location(
            null,
            Double.parseDouble(data[0]),
            Double.parseDouble(data[1]),
            Double.parseDouble(data[2])
        );
        else if (data.length == 4) return new org.bukkit.Location(
            Bukkit.getWorld(data[0]),
            Double.parseDouble(data[1]),
            Double.parseDouble(data[2]),
            Double.parseDouble(data[3])
        );
        else throw new IllegalStateException("Illegal location key: "+key+", show have 3 or 4 entries separated with \";\", but have "+data.length);
    }
    
}