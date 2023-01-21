package ru.ancap.framework.api.command.commands.operator.finite.pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import ru.ancap.framework.api.command.commands.transformer.TransformationException;
import ru.ancap.framework.api.command.commands.transformer.Transformer;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.dispatched.exception.NoNextArgumentException;
import ru.ancap.framework.api.event.classic.IncorrectArgumentEvent;
import ru.ancap.framework.api.event.classic.NotEnoughArgumentsEvent;


public class DoubleArgumentEventPattern<A, B> implements CommandEventPattern {

    private final BiTypeEventProvider<A, B> eventSource;
    private final Transformer<A> firstTransformer;
    private final Transformer<B> secondTransformer;

    public DoubleArgumentEventPattern(BiTypeEventProvider<A, B> eventSource, Transformer<A> firstTransformer, Transformer<B> secondTransformer) {
        this.eventSource = eventSource;
        this.firstTransformer = firstTransformer;
        this.secondTransformer = secondTransformer;
    }

    @Override
    public Event patternalize(CommandSender sender, LeveledCommand command) {
        String firstArg;
        String secondArg;
        try {
            firstArg = command.nextArgument();
            secondArg = command.withoutArgument().nextArgument();
            return this.eventSource.event(sender,
                    this.firstTransformer.transform(firstArg),
                    this.secondTransformer.transform(secondArg)
            );
        } catch (NoNextArgumentException e) {
            return new NotEnoughArgumentsEvent(sender, 1);
        } catch (TransformationException e) {
            return new IncorrectArgumentEvent(sender, e.getArgument());
        }
    }

    @FunctionalInterface
    public interface BiTypeEventProvider<A, B> {

        Event event(CommandSender sender, A ta, B tb);

    }
}
