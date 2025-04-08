package ru.ancap.framework.plugin.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.MeteredTask;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PluginLoadTask implements Runnable {
    
    @Delegate
    private final MeteredTask delegate;
    
    public PluginLoadTask(@NonNull JavaPlugin plugin, @NonNull CallableText taskName, @NonNull Runnable mainTask, @Nullable String startId, @Nullable String endId) {
        this(of(plugin, taskName, mainTask, startId, endId));
    }

    private static MeteredTask of(JavaPlugin plugin, CallableText taskName, Runnable mainTask, String startId, String endId) {
        Communicator communicator = Communicator.of(Bukkit.getConsoleSender());
        return new MeteredTask(
                () -> {
                    if (startId != null) communicator.message(new LAPIText(
                            startId,
                            new Placeholder("plugin", plugin.getName()),
                            new Placeholder("task", taskName)
                    ));
                },
                mainTask, 
                (duration) -> {
                    if (endId != null) communicator.message(new LAPIText(
                            endId, 
                            new Placeholder("plugin", plugin.getName()),
                            new Placeholder("task", taskName),
                            new Placeholder("time", duration.toMillis())
                    ));
                }
        );
    }

}