package ru.ancap.framework.plugin.util.scheduling;

import org.jetbrains.annotations.ApiStatus;

/**
 * More convenient way for running permatasks.
 * @param delay Time from server start to first execution.
 */
@ApiStatus.Experimental
public record Permatask(Runnable runnable, int delay, int period, boolean async) {
    
    // TODO builder methods?
    
}