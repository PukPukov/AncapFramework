package ru.ancap.framework.artifex.implementation.ancap;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.bukkit.entity.Player;
import ru.ancap.commons.instructor.Instructor;
import ru.ancap.commons.instructor.SimpleEventBus;
import ru.ancap.framework.artifex.implementation.command.center.CommandCatcherInstaller;
import ru.ancap.framework.artifex.implementation.plugin.ServerTPSCounter;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.plugin.api.Ancap;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.util.player.StepbackMaster;

import java.io.File;

@Getter
@ToString @EqualsAndHashCode
public class ArtifexAncap implements Ancap {
    
    private final ServerTPSCounter tpsCounter;
    private final StepbackMaster stepbackMaster;
    private final Instructor<Player> playerLeaveInstructor;
    private final File debugIndicator;
    
    private volatile boolean debug = false;
    
    public ArtifexAncap(ServerTPSCounter tpsCounter, StepbackMaster stepbackMaster, File debugIndicator) {
        this.tpsCounter = tpsCounter;
        this.stepbackMaster = stepbackMaster;
        this.debugIndicator = debugIndicator;
        this.playerLeaveInstructor = new SimpleEventBus<>();
    }

    @Override public StepbackMaster stepbackMaster() { return this.stepbackMaster; }
    @Override public Instructor<Player> playerLeaveInstructor() { return this.playerLeaveInstructor; }
    
    public void load() {
        this.debug = this.debugIndicator.exists();
    }

    @Override
    public void installGlobalCommandOperator(AncapPlugin owner, CommandOperator global, OperateRule scope) {
        CommandCatcherInstaller.install(owner, global, scope);
    }

    @Override
    public boolean debug() {
        return this.debug;
    }

    @Override
    @SneakyThrows
    public void setDebug(boolean debug) {
        this.debug = debug;
        if (debug) this.debugIndicator.createNewFile();
        else this.debugIndicator.delete();
    }

    @Override
    public double getServerTPS() {
        return this.tpsCounter.get();
    }

}