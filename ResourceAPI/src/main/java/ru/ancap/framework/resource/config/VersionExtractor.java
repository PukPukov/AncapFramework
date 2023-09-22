package ru.ancap.framework.resource.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

public record VersionExtractor(String versionFieldName) implements Function<ConfigurationSection, Integer> {

    public Integer apply(ConfigurationSection section) {
        return section.getInt(this.versionFieldName);
    }

}
