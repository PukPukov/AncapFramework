package ru.ancap.framework.language.loader;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.configuration.ConfigurationSection;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.loader.exception.LocaleLoaderException;

import java.util.List;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString @EqualsAndHashCode
public class YamlLocaleLoader {
    
    private final String lapiSection;
    private final ConfigurationSection yaml;
    
    public static void load(ConfigurationSection yaml, String lapiSection, String versionFieldName) {
        String languageCode = yaml.getString("language");
        if (languageCode == null) throw new LocaleLoaderException("Can't load locale without language code!");
        Language language = Language.of(languageCode);
        Set<String> keySet = yaml.getKeys(true);
        keySet.remove("language");
        keySet.remove(versionFieldName);
        for (String key : keySet) {
            if (yaml.isConfigurationSection(key)) continue;
            List<String> stringList = yaml.getStringList(key);
            if (stringList.isEmpty()){
                String string = yaml.getString(key);
                if (string != null) stringList = List.of(string);
                else throw new RuntimeException("????");
            }
            String string = stringList.stream().reduce(((s, s2) -> s+"\n"+s2)).get();
            LAPI.loadLocale(lapiSection != null ? lapiSection : "global", key, string, language);
        }
    }

}