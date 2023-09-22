package ru.ancap.framework.language.loader;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.configuration.ConfigurationSection;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.loader.exception.LocaleLoaderException;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class YamlLocaleLoader implements Runnable {

    private final ConfigurationSection section;

    public void run() {
        String languageCode = this.section.getString("language");
        if (languageCode == null) throw new LocaleLoaderException("Can't load locale without language code!");
        Language language = Language.of(languageCode);
        Set<String> keySet = this.section.getKeys(true);
        for (String key : keySet) {
            List<String> stringList = this.section.getStringList(key);
            if (stringList.isEmpty()){
                String string = this.section.getString(key);
                if (string != null) stringList = List.of(string);
                else throw new RuntimeException("????");
            }
            String string = stringList.stream().reduce(((s, s2) -> s+"\n"+s2)).get();
            LAPI.loadLocale(key, string, language);
        }
    }

}
