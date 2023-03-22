package ru.ancap.framework.command.api.commands.operator.exclusive;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class Permission implements Pass {
    
    private final String permission;
    
    @Override
    public boolean allows(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }
    
}
