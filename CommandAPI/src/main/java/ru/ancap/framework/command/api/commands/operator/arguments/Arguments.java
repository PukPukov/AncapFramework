package ru.ancap.framework.command.api.commands.operator.arguments;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.command.api.syntax.CSCommand;
import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;
import ru.ancap.framework.command.api.commands.object.tab.OptionalTab;
import ru.ancap.framework.command.api.commands.object.tab.TabBundle;
import ru.ancap.framework.command.api.commands.object.tab.TabCompletion;
import ru.ancap.framework.command.api.commands.operator.arguments.bundle.ArgumentsBundle;
import ru.ancap.framework.command.api.commands.operator.arguments.bundle.ArgumentsMap;
import ru.ancap.framework.command.api.commands.operator.arguments.command.ArgumentCommandDispatch;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.exception.TransformationException;
import ru.ancap.framework.command.api.event.classic.CannotTransformArgumentEvent;
import ru.ancap.framework.command.api.event.classic.NotEnoughArgumentsEvent;
import ru.ancap.framework.command.api.exception.classic.RequiredArgumentMissingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class Arguments implements CSCommandOperator {

    private final Consumer<Class<?>> onMissing;
    private final Map<Integer, ArgumentsShard> argumentBindings;
    private final List<Argument> arguments;
    private final int requiredLiterals;
    private final Consumer<ArgumentCommandDispatch> dispatchConsumer;
    
    public Arguments(Accept accept, Consumer<ArgumentCommandDispatch> dispatchConsumer) {
        this(
            (sender, lacked) -> { throw new RequiredArgumentMissingException(); },
            accept,
            dispatchConsumer
        );
    }

    public Arguments(Consumer<Class<?>> onMissing, Accept accept, Consumer<ArgumentCommandDispatch> dispatchConsumer) {
        this(onMissing, Arguments.bindingsFor(accept), accept, Arguments.requiredLiteralsAmountFor(accept), dispatchConsumer);
    }

    private static Map<Integer, ArgumentsShard> bindingsFor(List<Argument> arguments) {
        if (arguments.isEmpty()) throw new IllegalStateException("Arguments list cannot be empty!");
        Map<Integer, ArgumentsShard> map = new HashMap<>();
        int lastStartIndex = 0;
        for (Argument argument : arguments) {
            int size = argument.extractor().size();
            int endIndex = lastStartIndex + size;
            ArgumentsShard shard = new ArgumentsShard(lastStartIndex, endIndex, argument);
            for (int cursor = lastStartIndex; cursor < endIndex; cursor++) {
                map.put(cursor, shard);
            }
            lastStartIndex = endIndex;
        }
        return map;
    }

    private static int requiredLiteralsAmountFor(List<Argument> arguments) {
        int collected = 0;
        for (Argument argument : arguments) {
            if (argument.optional()) break;
            collected += argument.extractor().size();
        }
        return collected;
    }

    @Override
    public void on(CommandDispatch dispatch) {
        CSCommand command = dispatch.command();
        Map<String, Object> map = new HashMap<>();
        
        int argumentIndex = 0;
        
        if (dispatch.command().arguments().size() < this.requiredLiterals) {
            this.onMissing.accept(dispatch.source().sender(), this.requiredLiterals - dispatch.command().arguments().size());
            return;
        }
        
        while (!command.isRaw() && argumentIndex <= this.arguments.size() - 1) {
            Argument argument = this.arguments.get(argumentIndex);
            Object extracted;
            try {
                extracted = argument.extractor().extract(command);
            } catch (TransformationException exception) {
                Bukkit.getPluginManager().callEvent(new CannotTransformArgumentEvent(
                        dispatch.source().sender(), 
                        exception.base(), 
                        exception.extractor().type()
                ));
                return;
            }
            try {
                command = command.withoutArguments(argument.extractor().size());
            } catch (NoNextArgumentException exception) {
                if (argument.optional()) break; 
                else throw new IllegalStateException();
            }
            map.put(argument.argumentName(), extracted);
            argumentIndex ++;
        }
        
        ArgumentsBundle bundle = new ArgumentsMap(Map.copyOf(map));
        this.dispatchConsumer.accept(new ArgumentCommandDispatch(dispatch.source(), bundle));
    }

    @Override
    public void on(CommandWrite write) {
        
        // 0 name
        // 1 hex
        // 2 hex
        // 3 role
        // 4 time
        // 5 time
        // 6 time
        // 7 time
        // pidorchuk red 5005 assistant 1 [day week jopa]
        // PlayerExtractor(1), HexagonExtractor(2), RoleExtractor(1), TimeExtractor(2)
        
        CSCommand command = write.line();
        int written = command.arguments().size();
        
        // -1, потому что written - размер, а не индекс, +1, потому что надо получить следующий аргумент от последнего написанного
        ArgumentsShard shard = this.argumentBindings.get(written - 1 + 1);
        if (shard == null) return;
        
        ArgumentBounding bounding = this.optionalityBounding.get(shard.node().optional());

        Function<CommandSender, List<TabCompletion>> tabFunction = shard.node().help() != null ? 
            shard.node().help() :
            shard.node().extractor().help();
        
        write.speaker().sendTab(TabBundle.builder()
                .tooltiped(tabFunction.apply(write.speaker().source().sender()).stream()
                        .map(tabCompletion -> new OptionalTab(bounding.opening + tabCompletion.completion() + bounding.closing, tabCompletion.tooltipState()))
                        .collect(Collectors.toList()))
                .replace((written - 1) - shard.firstLiteralIndex())
                .build()
        );
    }
    
    private final Map<Boolean, ArgumentBounding> optionalityBounding = Map.of(
            true, new ArgumentBounding("[", "]"),
            false, new ArgumentBounding("<", ">")
    );

    private record ArgumentBounding(String opening, String closing) {}
    private record ArgumentsShard(int firstLiteralIndex, int lastLiteralIndex, Argument node) {}
    
}
