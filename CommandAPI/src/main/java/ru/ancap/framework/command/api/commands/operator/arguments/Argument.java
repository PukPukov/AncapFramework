package ru.ancap.framework.command.api.commands.operator.arguments;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.command.api.commands.object.tab.TabCompletion;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.ArgumentExtractor;
import ru.ancap.framework.communicate.message.CallableMessage;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Argument {
    
    private final String argumentName;
    private final ArgumentExtractor<?> argumentExtractor;
    private final boolean optional;
    private final Function<CommandSender, List<TabCompletion>> help;

    public Argument(String argumentName, ArgumentExtractor<?> argumentExtractor) {
        this(argumentName, argumentExtractor, false);
    }

    public Argument(String argumentName, ArgumentExtractor<?> argumentExtractor, boolean optional) {
        this(argumentName, argumentExtractor, optional, null);
    }
    
    public String argumentName() {
        return this.argumentName;
    }
    
    public ArgumentExtractor<?> extractor() {
        return this.argumentExtractor;
    }
    
    public boolean optional() {
        return this.optional;
    }
    
    public Function<CommandSender, List<TabCompletion>> help() {
        return this.help;
    }
    
}
