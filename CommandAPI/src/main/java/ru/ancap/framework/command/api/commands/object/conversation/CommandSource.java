package ru.ancap.framework.command.api.commands.object.conversation;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.communicate.communicator.Communicator;

public interface CommandSource {
    
    CommandSender sender();
    Audience audience();

    default Communicator communicator() {
        return Communicator.of(this.sender());
    }
    
}
