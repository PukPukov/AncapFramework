package ru.ancap.framework.api.command.commands.operator.arguments;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.event.CommandDispatch;
import ru.ancap.framework.api.command.commands.command.event.CommandWrite;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.framework.api.command.commands.operator.arguments.bundle.ArgumentsBundle;
import ru.ancap.framework.api.command.commands.operator.arguments.bundle.ArgumentsMap;
import ru.ancap.framework.api.command.commands.operator.arguments.command.ArgumentCommandDispatch;
import ru.ancap.framework.api.command.commands.transformer.TransformationException;
import ru.ancap.framework.api.command.util.TypeNameProvider;
import ru.ancap.framework.api.event.classic.CannotTransformArgumentEvent;
import ru.ancap.framework.api.event.classic.NotEnoughArgumentsEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class Arguments implements CommandOperator {
    
    private final CommandOperator rawDelegate;
    private final TypeNameProvider typeNameProvider;
    private final List<Argument> arguments;
    private final Consumer<ArgumentCommandDispatch> dispatchConsumer;
    
    public Arguments(
            TypeNameProvider typeNameProvider, 
            List<Argument> arguments,
            Consumer<ArgumentCommandDispatch> dispatchConsumer
    ) {
        this(
                null,
                typeNameProvider,
                arguments,
                dispatchConsumer
        );
    }

    @Override
    public void on(CommandDispatch dispatch) {
        LeveledCommand command = dispatch.dispatched();
        if (command.isRaw()) {
            if (rawDelegate == null) {
                Bukkit.getPluginManager().callEvent(
                        new NotEnoughArgumentsEvent(
                                dispatch.sender(), 
                                this.arguments.size()
                        )
                );
                return;
            }
            rawDelegate.on(dispatch);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        int argumentIndex = 0;
        while (!command.isRaw()) {
            String nextArgument = command.nextArgument();
            if (argumentIndex+1 > arguments.size()) {
                map.put(argumentIndex+"", nextArgument);
                continue;
            }
            Argument argument = arguments.get(argumentIndex);
            try {
                map.put(argument.getArgumentName(), argument.getTransformer().transform(nextArgument));
            } catch (TransformationException e) {
                new CannotTransformArgumentEvent(dispatch.sender(), e.getArgument(), e.getType()).callEvent();
                return;
            }
            command = command.withoutArgument();
            argumentIndex++;
        }
        if (argumentIndex+1 < arguments.size()) {
            if (!arguments.get(argumentIndex + 1).isOptional()) {
                new NotEnoughArgumentsEvent(dispatch.sender(), arguments.size() - argumentIndex).callEvent();
                return;
            }
            arguments.stream()
                    .skip(argumentIndex)
                    .forEach(argument -> map.put(argument.getArgumentName(), null));
        }
        ArgumentsBundle bundle = new ArgumentsMap(Map.copyOf(map));
        dispatchConsumer.accept(new ArgumentCommandDispatch(dispatch.sender(), bundle));
    }

    @Override
    public void on(CommandWrite write) {
        LeveledCommand command = write.getWritten();
        int argumentIndex = 0;
        while (!command.isRaw()) {
            argumentIndex++;
            command = command.withoutArgument();
        }
        if (argumentIndex+1 > arguments.size()) return;
        Argument entry = arguments.get(argumentIndex);
        write.getSpeaker().sendTabs(List.of(
                typeNameProvider.apply(
                        entry.getTransformer().type(),
                        write.getSpeaker()
                )
        ));
    }
    
}
