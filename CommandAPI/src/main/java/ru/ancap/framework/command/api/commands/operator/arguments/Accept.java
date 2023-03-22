package ru.ancap.framework.command.api.commands.operator.arguments;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.util.List;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class Accept implements List<Argument> {
    
    @Delegate
    private final List<Argument> list;
    
    public Accept(Argument... arguments) {
        this(List.of(arguments));
    }
    
}
