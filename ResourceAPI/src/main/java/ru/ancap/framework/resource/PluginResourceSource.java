package ru.ancap.framework.resource;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.commons.resource.ResourceSource;

import java.io.File;
import java.io.InputStream;

@AllArgsConstructor
public class PluginResourceSource<T> implements ResourceSource<T> {

    private final JavaPlugin plugin;
    private final ResourcePreparator<T> resourcePreparator;

    @Override
    @SneakyThrows
    public T getResource(String fileName) {
        File file = new File(this.plugin.getDataFolder().getPath(), fileName);
        InputStream base = this.plugin.getResource(fileName);
        return this.resourcePreparator.prepare(base, file);
    }
    
}
