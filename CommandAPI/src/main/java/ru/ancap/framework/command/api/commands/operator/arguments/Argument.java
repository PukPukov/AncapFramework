package ru.ancap.framework.command.api.commands.operator.arguments;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.ArgumentExtractor;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Argument {
    
    private final String argumentName;
    private final ArgumentExtractor<?> argumentExtractor;
    private final boolean optional;

    public Argument(String argumentName, ArgumentExtractor<?> argumentExtractor) {
        this(argumentName, argumentExtractor, false);
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
    
}
