package ru.ancap.framework.artifex.configuration;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import ru.ancap.commons.time.Day;
import ru.ancap.framework.language.language.Language;

import java.util.Calendar;
import java.util.TimeZone;

public class ArtifexConfig {

    private static ArtifexConfig loaded;

    @Getter
    private final ConfigurationSection section;

    public ArtifexConfig(ConfigurationSection section) {
        this.section = section;
    }

    public void load() {
        loaded = this;
    }

    public static ArtifexConfig loaded() {
        return loaded;
    }

    public Language defaultLanguage() {
        return Language.of(this.section.getString("language.default"));
    }

    public String getDatabaseDataAccessKey() {
        return this.section.getString("database.connection.type").toLowerCase();
    }

    public ConfigurationSection getDatabaseDriverDataSection() {
        return this.section.getConfigurationSection("database.data");
    }

    public ConfigurationSection getDatabaseConnectionSection() {
        return this.section.getConfigurationSection("database.connection");
    }

    public long dayTimerAbsolute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, this.section.getInt("everyday-timer.hour"));
        calendar.set(Calendar.MINUTE, this.section.getInt("everyday-timer.minute", 0));
        calendar.set(Calendar.SECOND, this.section.getInt("everyday-timer.second", 0));
        calendar.set(Calendar.MILLISECOND, this.section.getInt("everyday-timer.millisecond", 0));
        long millis = calendar.getTimeInMillis();
        if (millis < System.currentTimeMillis()) millis = millis + Day.MILLISECONDS;
        return millis;
    }
}
