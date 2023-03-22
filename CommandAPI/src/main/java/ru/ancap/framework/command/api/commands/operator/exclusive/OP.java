package ru.ancap.framework.command.api.commands.operator.exclusive;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.command.CommandSender;

@ToString @EqualsAndHashCode
public class OP implements Pass {
    
    @Override
    public boolean allows(CommandSender sender) {
        return sender.isOp();
    }
    
}
