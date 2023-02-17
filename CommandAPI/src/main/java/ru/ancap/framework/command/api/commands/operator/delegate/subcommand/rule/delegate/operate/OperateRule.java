package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate;

import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;

public interface OperateRule {

    boolean isOperate(LeveledCommand command);

}
