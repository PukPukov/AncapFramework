package ru.ancap.framework.plugin.util.scheduling;

import java.util.Optional;

/**
 * Runnable, period and async usually is "what is the task", not "how the task should be launched",
 * so if you will need to return task from method, you will most likely need to return all these parameters,
 * and this is only possible when all this data is unified to single object since Java support only single-object returns.
 */
public record BukkitScheduledTaskData(Runnable runnable, Optional<Integer> period, boolean async) {
    
    // TODO builder methods?
    
}