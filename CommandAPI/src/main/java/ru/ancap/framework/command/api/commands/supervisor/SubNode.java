package ru.ancap.framework.command.api.commands.supervisor;

import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;

import java.util.List;

public class SubNode implements SupervisedRule {

    @Override
    public boolean isOperate(LeveledCommand command) {
        return false;
    }

    @Override
    public CommandOperator delegated() {
        return null;
    }

    @Override
    public String readableNameId() {
        return null;
    }

    @Override
    public String descriptionId() {
        return null;
    }

    @Override
    public List<String> candidates() {
        return null;
    }
}
