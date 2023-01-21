package ru.ancap.framework.api.plugin.plugins.config;

import lombok.experimental.Delegate;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StreamConfig implements ConfigurationSection {
    
    @Delegate
    private final ConfigurationSection section;
    
    public StreamConfig(InputStream stream) {
        this.section = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
    }
    
}
