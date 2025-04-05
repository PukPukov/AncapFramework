package ru.ancap.framework.plugin.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class FieldOfViewChecker {
    
    // TODO split field of view to horizontal/vertical
    public static boolean isInFrontOf(Location viewer, Vector viewDirection, int fieldOfView, Location viewed) {
        Vector toDamager = viewed.toVector().subtract(viewer.toVector());
        toDamager.normalize();
        double angle = viewDirection.angle(toDamager);
        angle = Math.toDegrees(angle);
        return angle <= fieldOfView / 2.0;
    }
    
}