package ru.ancap.framework.util;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class AudienceProvider {
    
    private static BukkitAudiences bukkitAudiences;
    
    public static BukkitAudiences bukkitAudiences() {
        return AudienceProvider.bukkitAudiences;
    }
    
    public static void bukkitAudiences(BukkitAudiences bukkitAudiences) {
        AudienceProvider.bukkitAudiences = bukkitAudiences;
    }
    
}