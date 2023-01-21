package ru.ancap.framework.api.command.commands.operator.finite.pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;

public interface CommandEventPattern {

    Event patternalize(CommandSender sender, LeveledCommand command);

}
