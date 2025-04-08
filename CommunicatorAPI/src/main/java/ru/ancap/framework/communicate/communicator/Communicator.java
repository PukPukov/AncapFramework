package ru.ancap.framework.communicate.communicator;

import org.bukkit.command.CommandSender;
import ru.ancap.framework.communicate.message.CallableText;

/**
 * Interface between CallableText and platform receivers.
 */
public interface Communicator {
    
    static Communicator of(CommandSender sender) {
        return BaseCommunicator.of(sender);
    }
    
    void message(CallableText callable);
    
}