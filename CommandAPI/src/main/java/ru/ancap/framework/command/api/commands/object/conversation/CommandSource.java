package ru.ancap.framework.command.api.commands.object.conversation;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableText;

public interface CommandSource {
    
    String identifier();
    Communicator communicator();
    
    // PLATFORM
    CommandSender sender();
    Audience audience();
    
    // UTIL
    default void message(CallableText message) {
        this.communicator().message(message);
    }
    
}