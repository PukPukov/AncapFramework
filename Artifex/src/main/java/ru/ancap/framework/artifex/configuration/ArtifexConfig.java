package ru.ancap.framework.artifex.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import ru.ancap.commons.time.Day;
import ru.ancap.framework.language.language.Language;

import java.util.*;

@RequiredArgsConstructor @Getter
@Accessors(fluent = true)
public class ArtifexConfig {

    private static ArtifexConfig loaded;

    private final ConfigurationSection section;

    public void load() {
        loaded = this;
    }

    public static ArtifexConfig loaded() {
        return loaded;
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
    
    public Language nativeLanguage() {
        String value = this.section.getString("language.server-native");
        assert value != null;
        return Language.of(value);
    }
    
    public Map<Language, List<Language>> targetFallbackMap() {
        Map<Language, List<Language>> fallbackMap = new HashMap<>();
        ConfigurationSection targetSection = this.section.getConfigurationSection("language.fallback.target");
        assert targetSection != null;
        for (String key : targetSection.getKeys(false)) {
            Language language = Language.of(key);
            List<Language> fallbackLanguages = this.readListTreatingSingularAsEntry(targetSection, key).stream()
                .map(Language::of).toList();
            fallbackMap.put(language, fallbackLanguages);
        }
        return fallbackMap;
    }
    
    public List<Language> defaultFallback() {
        return this.readListTreatingSingularAsEntry(this.section, "language.fallback.default").stream()
            .map(Language::of).toList();
    }
    
    // separate arguments needed due to bukkit works not with convenient paths but fucking sections
    private @NotNull List<String> readListTreatingSingularAsEntry(ConfigurationSection localRoot, String path) {
        List<String> values;
        if (localRoot.isList(path)) values = localRoot.getStringList(path);
        else {
            String value = localRoot.getString(path);
            assert value != null;
            values = List.of(value);
        };
        return values;
    }
    
}