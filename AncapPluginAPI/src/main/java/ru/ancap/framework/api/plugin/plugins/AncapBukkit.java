package ru.ancap.framework.api.plugin.plugins;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AncapBukkit {
    
    public static JavaPlugin CORE_PLUGIN;

    public static void sendConsoleCommand(String command) {
        Bukkit.getScheduler().callSyncMethod(
                CORE_PLUGIN,
                () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    return Void.TYPE;
                }
        );
    }
}
