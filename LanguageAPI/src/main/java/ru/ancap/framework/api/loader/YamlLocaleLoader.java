package ru.ancap.framework.api.loader;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import ru.ancap.framework.api.LAPI;
import ru.ancap.framework.api.language.Language;
import ru.ancap.framework.api.loader.exception.LocaleLoaderException;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class YamlLocaleLoader implements Runnable {

    private final ConfigurationSection section;

    public void run() {
        String languageCode = this.section.getString("language");
        if (languageCode == null) {
            throw new LocaleLoaderException("Can't load locale without language code!");
        }
        Language language = Language.of(languageCode);
        Set<String> keySet = this.section.getKeys(true);
        for (String key : keySet) {
            List<String> stringList = this.section.getStringList(key);
            if (stringList.size() == 0){
                String string = this.section.getString(key);
                if (string != null) stringList = List.of(string);
                else throw new RuntimeException("????");
            }
            String string = stringList.stream().reduce(((s, s2) -> s+"\n"+s2)).get();
            LAPI.loadLocale(key, string, language);
        }
    }

}
