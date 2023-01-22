package ru.ancap.framework.api.command.commands.command.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;

@AllArgsConstructor
@Data
public class CommandDispatch {
    
    private final CommandSender sender;
    private final LeveledCommand command;
    
}
