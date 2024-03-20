package ru.ancap.framework.artifex.implementation.timer.heartbeat.exceptions;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class ArtifexHeartbeatAlreadyStartedException extends RuntimeException {

    public ArtifexHeartbeatAlreadyStartedException(String message) {
        super(message);
    }
}
