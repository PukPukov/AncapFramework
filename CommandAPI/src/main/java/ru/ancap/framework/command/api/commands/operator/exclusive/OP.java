package ru.ancap.framework.command.api.commands.operator.exclusive;

import org.bukkit.command.CommandSender;

public class OP implements Pass {
    
    @Override
    public boolean allows(CommandSender sender) {
        return sender.isOp();
    }
    
}
