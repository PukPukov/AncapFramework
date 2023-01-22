package ru.ancap.framework.api.command.commands.operator.arguments.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.command.commands.operator.arguments.bundle.ArgumentsBundle;

@AllArgsConstructor
@Data
public class ArgumentCommandDispatch {
    private final CommandSender sender;
    private final ArgumentsBundle arguments;
}
