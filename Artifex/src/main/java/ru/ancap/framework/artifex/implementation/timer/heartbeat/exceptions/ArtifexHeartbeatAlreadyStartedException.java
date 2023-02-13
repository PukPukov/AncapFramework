package ru.ancap.framework.artifex.implementation.timer.heartbeat.exceptions;

public class ArtifexHeartbeatAlreadyStartedException extends RuntimeException {

    public ArtifexHeartbeatAlreadyStartedException(String message) {
        super(message);
    }
}
