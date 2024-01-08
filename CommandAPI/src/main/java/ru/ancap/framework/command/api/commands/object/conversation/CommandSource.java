package ru.ancap.framework.command.api.commands.object.conversation;

import org.bukkit.command.CommandSender;
import ru.ancap.framework.communicate.communicator.Communicator;

public interface CommandSource {
    
    CommandSender sender();

    default Communicator communicator() {
        return Communicator.of(this.sender());
    }
    
}
