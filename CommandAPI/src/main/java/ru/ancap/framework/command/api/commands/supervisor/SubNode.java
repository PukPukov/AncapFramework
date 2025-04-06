package ru.ancap.framework.command.api.commands.supervisor;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;

import java.util.List;

@ToString @EqualsAndHashCode
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
    public LeveledCommand convert(LeveledCommand command) {
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