package ru.ancap.framework.api.plugin.plugins.config;

import lombok.experimental.Delegate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamConfig implements ConfigurationSection {
    
    @Delegate
    private final ConfigurationSection section;
    
    public StreamConfig(InputStream stream) {
        this.section = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
    }
    
}
