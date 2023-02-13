package ru.ancap.framework.command.api.commands.operator.delegator.subcommand.rule.delegate.operate;

import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;

public interface OperateRule {

    boolean isOperate(LeveledCommand command);

}
