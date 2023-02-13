package ru.ancap.framework.command.api.commands.object.conversation;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

public interface CommandSource {
    
    CommandSender sender();
    Audience audience();
    
}
