package ru.ancap.framework.command.api.example;

import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;

public class ExampleTarget extends CommandTarget {
    
    public ExampleTarget() {
        super(new Delegate(
            new SubCommand(
                "teleport-me",
                new 
            )
        ));
    }
    
}
