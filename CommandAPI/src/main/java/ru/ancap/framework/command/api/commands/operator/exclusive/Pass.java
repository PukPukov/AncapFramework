package ru.ancap.framework.command.api.commands.operator.exclusive;

import org.bukkit.command.CommandSender;

public interface Pass {
    
    boolean allows(CommandSender sender);
    
}
