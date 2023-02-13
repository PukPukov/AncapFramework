package ru.ancap.framework.plugin.api.commands;

import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.plugin.api.exception.CommandNotRegisteredException;

/**
 * Alternative command center.
 * Advantages over the loader from Bukkit depends on realization. By default, check AsyncPacketCommandCenter in AncapPlugin.
 */
public interface CommandCenter {

    void initialize(AncapPlugin plugin);
    void setExecutor(String commandName, CommandOperator executor) throws CommandNotRegisteredException;

}
