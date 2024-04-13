package ru.ancap.framework.language.locale;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.ancap.commons.map.GuaranteedMap;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.language.LocalisationStatistic;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class MapLocales implements Locales {

    private final Map<String /*section*/, List<Registration>> registrations = new GuaranteedMap<>(ArrayList::new);
    private final Map<Language, Map<String, String>> map = new GuaranteedMap<>(HashMap::new);
    private final Language defaultLanguage;

    @Override
    public void loadLocale(@NonNull String section, @NonNull String id, @NonNull String localized, @NonNull Language language) {
        var languageMap = this.map.get(language);
        String oldMapData = languageMap.get(id);
        languageMap.put(id, localized);
        this.registrations.get(section).add(new Registration(language, id));
        if (oldMapData != null && !oldMapData.equals(localized)) {
            Logger.getGlobal().warning("Replaced "+id+"'s locale \""+oldMapData+"\" with "+localized);
        }
    }
    
    @Override
    public void drop(String section) {
        List<Registration> sectionRegistration = this.registrations.get(section);
        for (Registration registration : sectionRegistration) {
            this.map.get(registration.language).remove(registration.id);
        }
        this.registrations.put(section, new ArrayList<>());
    }
    
    @Override
    public @NonNull String localized(@NonNull String id, @NonNull Language language) {
        var languageMap = this.map.get(language);
        String localized = languageMap.get(id);
        if (localized != null) return localized;
        languageMap = this.map.get(this.defaultLanguage);
        localized = languageMap.get(id);
        if (localized != null) return localized;
        return language.code()+":"+id;
    }
    
    @Override
    public Set<Language> allLanguages() {
        return this.map.keySet().stream()
            .filter(key -> !this.map.get(key).isEmpty())
            .collect(Collectors.toSet());
    }
    
    @Override
    public LocalisationStatistic statistic(Language language) {
        return new LocalisationStatistic(this.map.get(language).size());
    }
    
    @Override
    public Set<String> allKeys(Language language) {
        return this.map.get(language).keySet();
    }
    
    private record Registration(Language language, String id) { }
    
}