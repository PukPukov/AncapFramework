package ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.tab.Tab;
import ru.ancap.framework.command.api.commands.object.tab.TabCompletion;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.ArgumentExtractor;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.exception.TransformationException;
import ru.ancap.framework.language.additional.LAPIMessage;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class PrimitiveExtractor<T> implements ArgumentExtractor<T> {
    
    private final Class<T> type;
    
    @Override
    public Function<CommandSender, List<TabCompletion>> help() {
        return sender -> List.of(
                new Tab(new LAPIMessage("ru.ancap.types."+this.type.getName()).call(sender.getName()))
        );
    }
    
    @Override
    public Class<?> type() {
        return this.type;
    }
    
    @Override
    public int size() {
        return 1;
    }

    @Override
    public T extract(LeveledCommand command) throws TransformationException {
        String argument = command.nextArgument();
        try {
            return this.provide(argument);
        } catch (Throwable throwable) {
            throw new TransformationException(this, List.of(argument));
        }
    }
    
    protected abstract T provide(String string) throws Throwable;
    
}
