package ru.ancap.framework.command.api.commands.operator.arguments.extractor;

import org.bukkit.command.CommandSender;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.tab.TabCompletion;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.exception.TransformationException;

import java.util.List;
import java.util.function.Function;

public interface ArgumentExtractor<T> {

    Function<CommandSender, List<TabCompletion>> help();
    Class<?> type();
    int size();
    T extract(LeveledCommand command) throws TransformationException;

}
