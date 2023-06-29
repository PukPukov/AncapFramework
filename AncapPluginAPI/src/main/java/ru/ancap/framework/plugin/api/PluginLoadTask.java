package ru.ancap.framework.plugin.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.MeteredTask;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PluginLoadTask implements Runnable {
    
    @Delegate
    private final MeteredTask delegate;
    
    public PluginLoadTask(@NotNull JavaPlugin plugin, @NotNull CallableMessage taskName, @NotNull Runnable mainTask, @Nullable String startId, @Nullable String endId) {
        this(of(plugin, taskName, mainTask, startId, endId));
    }

    private static MeteredTask of(JavaPlugin plugin, CallableMessage taskName, Runnable mainTask, String startId, String endId) {
        Communicator communicator = Communicator.of(Bukkit.getConsoleSender());
        return new MeteredTask(
                () -> {
                    if (startId != null) communicator.message(new LAPIMessage(
                            startId,
                            new Placeholder("plugin", plugin.getName()),
                            new Placeholder("task", taskName)
                    ));
                },
                mainTask, 
                (duration) -> {
                    if (endId != null) communicator.message(new LAPIMessage(
                            endId, 
                            new Placeholder("plugin", plugin.getName()),
                            new Placeholder("task", taskName),
                            new Placeholder("time", duration)
                    ));
                }
        );
    }

}
