package ru.ancap.framework.language.loader;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.ApiStatus;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.loader.exception.LocaleLoaderException;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class YamlLocaleLoader implements Runnable {
    
    private final String lapiSection;
    private final ConfigurationSection yaml;
    
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public YamlLocaleLoader(ConfigurationSection yaml) {
        this(null, yaml);
    }
    
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public void run() {
        this.load();
    }
    
    public void load() {
        String languageCode = this.yaml.getString("language");
        if (languageCode == null) throw new LocaleLoaderException("Can't load locale without language code!");
        Language language = Language.of(languageCode);
        Set<String> keySet = this.yaml.getKeys(true);
        keySet.remove("language");
        for (String key : keySet) {
            if (this.yaml.isConfigurationSection(key)) continue;
            List<String> stringList = this.yaml.getStringList(key);
            if (stringList.isEmpty()){
                String string = this.yaml.getString(key);
                if (string != null) stringList = List.of(string);
                else throw new RuntimeException("????");
            }
            String string = stringList.stream().reduce(((s, s2) -> s+"\n"+s2)).get();
            LAPI.loadLocale(this.lapiSection != null ? this.lapiSection : "global", key, string, language);
        }
    }

}