package ru.ancap.framework.command.api.commands.supervisor;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.syntax.CSCommand;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;

import java.util.List;

@ToString @EqualsAndHashCode
public class SubNode implements SupervisedRule {

    @Override
    public boolean isOperate(CSCommand command) {
        return false;
    }

    @Override
    public CSCommandOperator delegated() {
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
