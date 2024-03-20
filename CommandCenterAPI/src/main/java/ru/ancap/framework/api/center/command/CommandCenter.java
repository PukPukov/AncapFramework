package ru.ancap.framework.api.center.command;

import java.util.List;
import java.util.Optional;

/**
 * Alternative command center with bigger amount of game features supported
 */
public interface CommandCenter {
    
    /**
     * Makes exclusive registration attempt to the selected ID.
     *
     * @throws ru.ancap.framework.api.center.command.exception.CommandAlreadyRegisteredException
     */
    CommandRegistrationBuilder register(String id);
    
    /**
     * @throws ru.ancap.framework.api.center.command.exception.CommandNotRegisteredException
     */
    void unregister(String id);
    
    Optional<CommandRegistrationState> read(String id);
    List<CommandRegistrationState> readAll();
    
}