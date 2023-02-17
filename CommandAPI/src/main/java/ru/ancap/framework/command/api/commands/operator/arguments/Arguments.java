package ru.ancap.framework.command.api.commands.operator.arguments;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.object.tab.OptionalTab;
import ru.ancap.framework.command.api.commands.object.tab.TabBundle;
import ru.ancap.framework.command.api.commands.operator.arguments.bundle.ArgumentsBundle;
import ru.ancap.framework.command.api.commands.operator.arguments.bundle.ArgumentsMap;
import ru.ancap.framework.command.api.commands.operator.arguments.command.ArgumentCommandDispatch;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.exception.TransformationException;
import ru.ancap.framework.command.api.event.classic.CannotTransformArgumentEvent;
import ru.ancap.framework.command.api.event.classic.NotEnoughArgumentsEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Arguments implements CommandOperator {

    private final BiConsumer<CommandSender, Integer> onNotEnough;
    private final Map<Integer, ArgumentsShard> argumentBindings;
    private final List<Argument> arguments;
    private final int requiredLiterals;
    private final Consumer<ArgumentCommandDispatch> dispatchConsumer;

    public Arguments(BiConsumer<CommandSender, Integer> onNotEnough, List<Argument> arguments, Consumer<ArgumentCommandDispatch> dispatchConsumer) {
        this(onNotEnough, Arguments.bindingsFor(arguments), arguments, Arguments.requiredLiteralsAmountFor(arguments), dispatchConsumer);
    }
    
    public Arguments(Accept accept, Consumer<ArgumentCommandDispatch> dispatchConsumer) {
        this((sender, lacked) -> Bukkit.getPluginManager().callEvent(new NotEnoughArgumentsEvent(sender, lacked)), accept, dispatchConsumer);
    }

    private static Map<Integer, ArgumentsShard> bindingsFor(List<Argument> arguments) {
        if (arguments.size() == 0) throw new IllegalStateException("Arguments list cannot be empty!");
        Map<Integer, ArgumentsShard> map = new HashMap<>();
        int lastStartIndex = 0;
        for (Argument argument : arguments) {
            int size = argument.extractor().size();
            int endIndex = lastStartIndex + size;
            ArgumentsShard shard = new ArgumentsShard(lastStartIndex, endIndex, argument);
            for (int cursor = lastStartIndex; cursor < endIndex; cursor++) map.put(cursor, shard);
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
        LeveledCommand command = dispatch.command();
        Map<String, Object> map = new HashMap<>();
        
        int argumentIndex = 0;
        
        if (dispatch.command().arguments().size() < this.requiredLiterals) this.onNotEnough.accept(dispatch.source().sender(), this.requiredLiterals - dispatch.command().arguments().size());
        
        while (!command.isRaw() && argumentIndex <= this.arguments.size() - 1) {
            Argument argument = this.arguments.get(0);
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
        
        LeveledCommand command = write.line();
        int written = command.arguments().size();
        
        // -1, потому что written - размер, а не индекс, +1, потому что надо получить следующий аргумент от последнего написанного
        ArgumentsShard shard = this.argumentBindings.get(written - 1 + 1);
        
        ArgumentBounding bounding = this.optionalityBounding.get(shard.node().optional());
        
        write.speaker().sendTab(TabBundle.builder()
                .tooltiped(shard.node().extractor().help().apply(write.speaker().source().sender()).stream()
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
    
    @AllArgsConstructor
    private static class ArgumentBounding {
        
        private final String opening;
        private final String closing;
        
        public String opening() {
            return this.opening;
        }
        
        public String closing() {
            return this.closing;
        }
        
    }
    
    @AllArgsConstructor
    private static class ArgumentsShard {
        
        private final int firstLiteralIndex;
        private final int lastLiteralIndex;
        private final Argument node;
        
        public int firstLiteralIndex() {
            return this.firstLiteralIndex;
        }
        
        public int lastLiteralIndex() {
            return this.lastLiteralIndex;
        }
        
        public Argument node() {
            return this.node;
        }
        
    }
    
}
