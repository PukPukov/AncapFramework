package ru.ancap.framework.plugin;

import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.api.command.util.TypeNameProvider;
import ru.ancap.framework.api.database.DatabaseConnectionProperty;
import ru.ancap.framework.api.plugin.plugins.Ancap;
import ru.ancap.framework.api.plugin.plugins.ResourceSource;
import ru.ancap.framework.plugin.resource.ArtifexResourceSource;

import java.util.Map;

@AllArgsConstructor
public class ArtifexAncap implements Ancap {

    private final Map<String, DatabaseConnectionProperty> globalProperties;
    private final TypeNameProvider typeNameProvider;

    @Override
    public Map<String, DatabaseConnectionProperty> getGlobalDatabaseProperties() {
        return globalProperties;
    }

    @Override
    public ResourceSource newResourceSource(JavaPlugin plugin, boolean saveFiles) {
        return new ArtifexResourceSource(plugin, saveFiles);
    }

    @Override
    public TypeNameProvider getTypeNameProvider() {
        return typeNameProvider;
    }
}
