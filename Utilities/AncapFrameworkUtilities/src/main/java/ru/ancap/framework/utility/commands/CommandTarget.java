package ru.ancap.framework.utility.commands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import ru.ancap.framework.api.center.command.CommandEventHandler;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;

@Accessors(fluent = true) @Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandTarget implements CSCommandOperator, CommandEventHandler {

    @Delegate private final CSCommandOperator base;
    @Delegate private final CommandEventHandler wrapper;

    public CommandTarget(CSCommandOperator base) {
        this.base = base;
        this.wrapper = new CSAPIEventHandler(base);
    }

    public CommandTarget(CommandTarget target) {
        this(target.base, target.wrapper);
    }
    
}