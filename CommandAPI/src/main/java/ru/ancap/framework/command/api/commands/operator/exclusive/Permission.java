package ru.ancap.framework.command.api.commands.operator.exclusive;

import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class Permission implements Pass {
    
    private final String permission;
    
    @Override
    public boolean allows(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }
    
}
