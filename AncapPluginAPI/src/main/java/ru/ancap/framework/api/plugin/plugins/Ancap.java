package ru.ancap.framework.api.plugin.plugins;

import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.api.command.util.TypeNameProvider;
import ru.ancap.framework.api.database.DatabaseConnectionProperty;

import java.util.Map;

public interface Ancap {
    
    Map<String, DatabaseConnectionProperty> getGlobalDatabaseProperties();
    ResourceSource newResourceSource(JavaPlugin plugin, boolean saveFiles);
    TypeNameProvider getTypeNameProvider();

}
