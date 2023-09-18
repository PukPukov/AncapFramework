package ru.ancap.framework.command.api.example;

import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;

public class ExampleTarget extends CommandTarget {
    
    public ExampleTarget() {
        super(new Delegate()); // TODO
    }
    
}
