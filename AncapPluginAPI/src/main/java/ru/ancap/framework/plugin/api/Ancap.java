package ru.ancap.framework.plugin.api;

import org.bukkit.entity.Player;
import ru.ancap.commons.instructor.Instructor;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.util.player.StepbackMaster;

public interface Ancap {
    
    boolean debug();
    void setDebug(boolean debug);
    double getServerTPS();
    void installGlobalCommandOperator(AncapPlugin owner, CommandOperator global, OperateRule scope);
    StepbackMaster stepbackMaster();
    Instructor<Player> playerLeaveInstructor();

}