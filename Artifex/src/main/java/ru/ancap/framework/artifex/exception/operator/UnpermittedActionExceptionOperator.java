package ru.ancap.framework.artifex.exception.operator;

import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.exception.exception.UnpermittedActionException;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandExceptionOperator;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.command.api.commands.exception.CommandErrorText;

import java.util.ArrayList;
import java.util.List;

public class UnpermittedActionExceptionOperator implements CommandExceptionOperator<UnpermittedActionException> {
    
    @Override
    public void on(UnpermittedActionException exception, CommandSource source, LeveledCommand leveledCommand) {
        List<Placeholder> placeholders = new ArrayList<>(2);
        placeholders.add(new Placeholder(
            "base", new LAPIText(
            Artifex.class, "error.not-enough-permissions.base",
            new Placeholder("action", exception.actionDescription() != null ?
                exception.actionDescription() :
                new LAPIText(Artifex.class, "error.not-enough-permissions.default-action")
            )
        )
        ));
        if (exception.requiredPermission() != null) placeholders.add(new Placeholder("requirement", exception.requiredPermission()));
        CommandErrorText.send(source.sender(), new LAPIText(
            Artifex.class, exception.requiredPermission() != null ?
            "error.not-enough-permissions.form.requirement" :
            "error.not-enough-permissions.form.simple",
            placeholders.toArray(new Placeholder[0])
        ));
    }
    
}