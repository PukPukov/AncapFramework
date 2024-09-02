package ru.ancap.framework.plugin.api;

import org.bukkit.Bukkit;

public class PaperShitCounteractions {
    
    public static String craftBukkitPackage() {
        return Bukkit.getServer().getClass().getPackage().getName();
    }
    
}