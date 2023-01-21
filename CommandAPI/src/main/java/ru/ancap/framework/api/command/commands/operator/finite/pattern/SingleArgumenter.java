package ru.ancap.framework.api.command.commands.operator.finite.pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.dispatched.exception.NoNextArgumentException;
import ru.ancap.framework.api.event.classic.NotEnoughArgumentsEvent;

public class SingleArgumenter implements CommandEventPattern {

    private final StringEventProvider eventSource;

    public SingleArgumenter(StringEventProvider provider) {
        this.eventSource = provider;
    }

    @Override
    public Event patternalize(CommandSender sender, LeveledCommand command) {
        try {
            String argument = command.nextArgument();
            return this.eventSource.event(sender, argument);
        } catch (NoNextArgumentException e) {
            return new NotEnoughArgumentsEvent(sender, 1);
        }
    }

    @FunctionalInterface
    public interface StringEventProvider {
        Event event(CommandSender sender, String argument);
    }
}
