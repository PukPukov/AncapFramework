package ru.ancap.framework.command.api.example;

import ru.ancap.framework.command.api.commands.CSCommandTarget;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;

public class ExampleTarget extends CSCommandTarget {
    
    public ExampleTarget() {
        super(new Delegate()); // TODO
    }
    
}
