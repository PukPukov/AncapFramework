package ru.ancap.framework.plugin.api;

import org.bukkit.Bukkit;

public class PaperShitWorkarounds {
    
    public static String craftBukkitPackage() {
        return Bukkit.getServer().getClass().getPackage().getName();
    }
    
}