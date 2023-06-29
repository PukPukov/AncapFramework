package ru.ancap.framework.plugin.api.commands;

import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.plugin.api.exception.CommandNotRegisteredException;

/**
 * Alternative command center.
 * Benefits over the loader from Bukkit depends on realization. By default, check AsyncPacketCommandCenter in AncapPlugin.
 */
public interface CommandCenter {

    void initialize(AncapPlugin plugin);
    
    void register(String commandName, CommandHandleState state);
    void unregister(String commandName);
    void setExecutor(String commandName, CommandOperator operator) throws CommandNotRegisteredException;
    
    @Nullable CommandHandleState findRegisterStateOf(String commandName);
    
}
