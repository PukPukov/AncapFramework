package ru.ancap.framework.plugin.api.commands;

import ru.ancap.framework.api.center.command.CommandRegistrationBuilder;
import ru.ancap.framework.api.center.command.exception.CommandAlreadyRegisteredException;

public interface PluginPrivateCommandRegistrar {
    
    /**
     * @throws CommandAlreadyRegisteredException
     */
    CommandRegistrationBuilder register(String key);
    
}