package ru.ancap.framework.command.api.commands.operator.arguments.extractor;

import org.bukkit.command.CommandSender;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.exception.TransformationException;
import ru.ancap.framework.command.api.syntax.CSCommand;
import ru.ancap.framework.mccsyntax.bukkitadv.tab.TabSuggestion;

import java.util.List;
import java.util.function.Function;

public interface ArgumentExtractor<T> {

    Function<CommandSender, List<TabSuggestion>> help();
    Class<?> type();
    T extract(CSCommand command) throws TransformationException;

}
