package ru.ancap.framework.plugin.api.commands;

import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.plugin.api.AncapPlugin;

import java.util.List;
import java.util.Set;

/**
 * Alternative command center.
 * Benefits over the loader from Bukkit depends on realization. By default, check AsyncPacketCommandCenter in AncapPlugin.
 */
public interface CommandCenter {

    void initialize(AncapPlugin plugin);
    
    void register(String id, List<String> sources, CommandHandleState state);
    void unregister(String id);
    void setExecutor(String id, CommandOperator operator);
    
    @Nullable CommandData findDataOf(String commandName);
    Set<String> findRegisteredCommandsOf(AncapPlugin plugin);
    
}
