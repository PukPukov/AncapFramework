package ru.ancap.framework.plugin.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;

public class SoundUtil {
    
    /**
     * Less cluttered sound play method.
     */
    public static void play(String soundKey, Location location) {
        location.getWorld().playSound(
            Sound.sound(Key.key(soundKey), Sound.Source.MASTER, 1, 1),
            location.x(), location.y(), location.z()
        );
    }
    
}