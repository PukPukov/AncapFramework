package ru.ancap.framework.util.configuration;

import lombok.experimental.Delegate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamConfiguration implements ConfigurationSection {
    
    @Delegate
    private final ConfigurationSection section;
    
    public StreamConfiguration(InputStream stream) {
        this.section = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
    }
    
}