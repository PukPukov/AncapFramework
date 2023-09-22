package ru.ancap.framework.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

@UtilityClass
public class AudienceProvider {
    
    private static BukkitAudiences bukkitAudiences;
    
    public static BukkitAudiences bukkitAudiences() {
        return AudienceProvider.bukkitAudiences;
    }
    
    public static void bukkitAudiences(BukkitAudiences bukkitAudiences) {
        AudienceProvider.bukkitAudiences = bukkitAudiences;
    }
    
}
