package ru.ancap.framework.plugin.api;

import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;

public interface Ancap {
    
    double getServerTPS();
    void installGlobalCommandOperator(AncapPlugin owner, CommandOperator global, OperateRule scope);

}