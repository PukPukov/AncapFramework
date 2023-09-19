package ru.ancap.framework.language.locale;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.ancap.framework.language.language.Language;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@ToString @EqualsAndHashCode
public class MapLocales implements Locales {

    private final Map<Language, Map<String, String>> map;
    private final Language defaultLanguage;

    public MapLocales(Language defaultLanguage) {
        this.map = new HashMap<>();
        this.defaultLanguage = defaultLanguage;
    }

    @Override
    public void loadLocale(@NonNull String id, @NonNull String localized, @NonNull Language language) {
        this.fillLanguage(language);
        var languageMap = this.map.get(language);
        String oldMapData = languageMap.get(id);
        languageMap.put(id, localized);
        if (oldMapData != null && !oldMapData.equals(localized)) {
            Logger.getGlobal().warning("Replaced "+id+"'s locale \""+oldMapData+"\" with "+localized);
        }
    }

    @Override
    public @NonNull String localized(@NonNull String id, @NonNull Language language) {
        this.fillLanguage(language);
        var languageMap = this.map.get(language);
        String localized = languageMap.get(id);
        if (localized != null) return localized;
        languageMap = this.map.get(defaultLanguage);
        localized = languageMap.get(id);
        if (localized != null) return localized;
        return language.code()+":"+id;
    }

    private void fillLanguage(Language language) {
        this.map.computeIfAbsent(language, k -> new HashMap<>());
    }
    
}
