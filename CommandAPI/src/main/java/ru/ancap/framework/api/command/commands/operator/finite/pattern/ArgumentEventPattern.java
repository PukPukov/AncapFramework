package ru.ancap.framework.api.command.commands.operator.finite.pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.dispatched.exception.NoNextArgumentException;
import ru.ancap.framework.api.command.commands.transformer.TransformationException;
import ru.ancap.framework.api.command.commands.transformer.Transformer;
import ru.ancap.framework.api.event.classic.IncorrectArgumentEvent;
import ru.ancap.framework.api.event.classic.NotEnoughArgumentsEvent;

public class ArgumentEventPattern<T> implements CommandEventPattern {

    private final TypeEventProvider<T> eventSource;
    private final Transformer<T> transformer;

    public ArgumentEventPattern(TypeEventProvider<T> eventSource, Transformer<T> baseTransformer) {
        this.eventSource = eventSource;
        this.transformer = baseTransformer;
    }

    @Override
    public Event patternalize(CommandSender sender, LeveledCommand command) {
        String argument;
        try {
            argument = command.nextArgument();
            return this.eventSource.event(sender, this.transformer.transform(argument));
        } catch (NoNextArgumentException e) {
            return new NotEnoughArgumentsEvent(sender, 1);
        } catch (TransformationException e) {
            return new IncorrectArgumentEvent(sender, e.getArgument());
        }
    }

    @FunctionalInterface
    public interface TypeEventProvider<T> {
        Event event(CommandSender sender, T argument);
    }
}
