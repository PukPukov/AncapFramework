package ru.ancap.framework.command.api.commands;

import lombok.experimental.Delegate;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;


public class CSCommandTarget implements CSCommandOperator {

    @Delegate
    private final CSCommandOperator executor;

    public CSCommandTarget(CSCommandOperator executor) {
        this.executor = executor;
    }

    public CSCommandTarget(CSCommandTarget target) {
        this(target.executor);
    }

    protected CSCommandOperator getExecutor() {
        return this.executor;
    }
    
}
