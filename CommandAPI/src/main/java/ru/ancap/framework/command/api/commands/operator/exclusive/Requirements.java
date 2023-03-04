package ru.ancap.framework.command.api.commands.operator.exclusive;

import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor
public class Requirements implements Pass {
    
    private final List<Pass> requirements;
    
    public Requirements(Pass... requirements) {
        this(List.of(requirements));
    }
    
    @Override
    public boolean allows(CommandSender sender) {
        for (Pass pass : this.requirements) if (!pass.allows(sender)) return false;
        return true;
    }
}
