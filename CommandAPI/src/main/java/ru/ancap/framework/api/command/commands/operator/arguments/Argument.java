package ru.ancap.framework.api.command.commands.operator.arguments;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ancap.framework.api.command.commands.transformer.Transformer;

@Data
@AllArgsConstructor
public class Argument {
    
    private final String argumentName;
    private final Transformer<?> transformer;
    private final boolean optional;

    public Argument(String argumentName, Transformer<?> transformer) {
        this(argumentName, transformer, false);
    }
    
}
