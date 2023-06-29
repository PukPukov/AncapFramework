package ru.ancap.framework.resource;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.resource.ResourceSource;

import java.io.File;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GuaranteedPluginResourceSource<T> implements ResourceSource<T> {

    private final JavaPlugin plugin;
    private final ResourcePreparator<T> resourcePreparator;

    @Override
    @SneakyThrows
    public @Nullable T getResource(String fileName) {
        File file = new File(this.plugin.getDataFolder().getPath(), fileName);
        if (!file.exists()) file.createNewFile();
        return this.resourcePreparator.prepare(this.plugin.getResource(fileName), file);
    }

}