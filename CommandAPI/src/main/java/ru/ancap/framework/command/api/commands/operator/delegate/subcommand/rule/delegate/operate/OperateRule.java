package ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate;

import ru.ancap.framework.command.api.syntax.CSCommand;

public interface OperateRule {

    boolean isOperate(CSCommand command);

}
