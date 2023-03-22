package ru.ancap.framework.command.api.commands.operator.exclusive;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor
@ToString @EqualsAndHashCode
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
