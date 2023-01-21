package ru.ancap.framework.plugin.heartbeat.exceptions;

public class ArtifexHeartbeatAlreadyStartedException extends RuntimeException {

    public ArtifexHeartbeatAlreadyStartedException(String message) {
        super(message);
    }
}
