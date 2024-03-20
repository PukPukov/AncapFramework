package ru.ancap.framework.artifex.implementation.timer.heartbeat;

import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.artifex.implementation.timer.heartbeat.exceptions.ArtifexHeartbeatAlreadyStartedException;

public class ArtifexHeartbeat {

    private static volatile boolean started = false;

    private final JavaPlugin owner;

    public ArtifexHeartbeat(JavaPlugin owner) {
        this.owner = owner;
    }

    public ArtifexHeartbeat(ArtifexHeartbeat heartbeat) {
        this(heartbeat.getOwner());
    }

    protected JavaPlugin getOwner() {
        return this.owner;
    }

    public void start() {
        this.validateStart();
        this.loadRunnable();
        this.finishLoading();
    }

    private void validateStart() {
        if (ArtifexHeartbeat.started) {
            throw new ArtifexHeartbeatAlreadyStartedException("AncapHeartbeat is already started");
        }
    }

    private void loadRunnable() {
        ArtifexHeartbeatRunnable runnable = new ArtifexHeartbeatRunnable();
        runnable.runTaskTimer(this.getOwner(), 0, 20L);
    }

    private void finishLoading() {
        ArtifexHeartbeat.setStarted();
    }

    private static synchronized void setStarted() {
        ArtifexHeartbeat.started = true;
    }

}
