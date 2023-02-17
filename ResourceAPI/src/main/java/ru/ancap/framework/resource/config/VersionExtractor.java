package ru.ancap.framework.resource.config;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

@AllArgsConstructor
public class VersionExtractor implements Function<ConfigurationSection, Integer> {
    
    private final String versionFieldName;
    
    public Integer apply(ConfigurationSection section) {
        return section.getInt(this.versionFieldName);
    }
    
    public String versionFieldName() {
        return this.versionFieldName;
    }
    
}
