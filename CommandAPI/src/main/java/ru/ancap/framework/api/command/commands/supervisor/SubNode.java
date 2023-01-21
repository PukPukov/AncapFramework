package ru.ancap.framework.api.command.commands.supervisor;

import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;

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
