package ru.ancap.framework.artifex.implementation.ancap;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.ancap.framework.artifex.implementation.command.center.CommandCatcher;
import ru.ancap.framework.artifex.implementation.command.center.CommandCatcherInstaller;
import ru.ancap.framework.artifex.implementation.plugin.ServerTPSCounter;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.plugin.api.Ancap;
import ru.ancap.framework.plugin.api.AncapPlugin;

@RequiredArgsConstructor @Getter
@ToString @EqualsAndHashCode
public class ArtifexAncap implements Ancap {
    
    private final ServerTPSCounter tpsCounter;

    @Override
    public double getServerTPS() {
        return this.tpsCounter.get();
    }

    @Override
    public void installGlobalCommandOperator(AncapPlugin owner, CommandOperator global, OperateRule scope) {
        CommandCatcherInstaller.install(new CommandCatcher(this, owner, global, scope), owner);
    }

}
