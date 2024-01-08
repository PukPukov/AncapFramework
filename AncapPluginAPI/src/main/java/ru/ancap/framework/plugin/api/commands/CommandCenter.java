package ru.ancap.framework.plugin.api.commands;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Alternative command center.
 * Benefits over the loader from Bukkit depends on realization. By default, check AsyncPacketCommandCenter in AncapPlugin.
 */
public interface CommandCenter {
    
    CommandCenterDataPatch register(String id);
    CommandCenterDataPatch override(String id);
    CommandCenterDataPatch edit(String id);
    
    void unregister(String id);
    
    @Nullable CommandData findDataOf(String id);
    Set<String> findRegisteredCommandsOf(JavaPlugin plugin);
    
}
