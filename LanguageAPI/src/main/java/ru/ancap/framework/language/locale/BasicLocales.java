package ru.ancap.framework.language.locale;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.map.GuaranteedMap;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.language.LocalisationStatistic;
import ru.ancap.framework.language.locale.util.LazyFlatteningIterator;

import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class BasicLocales implements Locales {
    
    public static volatile Logger defaultLogger = Bukkit.getLogger(); // backward compatibility

    private final Map<String /*section*/, List<Registration>> registrations = new GuaranteedMap<>(ArrayList::new);
    private final Map<Language, Map<String, String>> map = new GuaranteedMap<>(HashMap::new);
    private final Map<Language, List<Language>> targetFallback;
    private final List<Language> defaultFallback;
    private final Language native_;

    @Override
    public void loadLocale(@NonNull String section, @NonNull String id, @NonNull String localized, @NonNull Language language) {
        var languageMap = this.map.get(language);
        String oldMapData = languageMap.get(id);
        languageMap.put(id, localized);
        this.registrations.get(section).add(new Registration(language, id));
        if (oldMapData != null && !oldMapData.equals(localized)) {
            defaultLogger.warning("Overrided locale with id \""+id+"\" in section \""+section+"\"");
            defaultLogger.warning("  from \""+oldMapData+"\"\n");
            defaultLogger.warning("  with \""+localized+"\"");
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
        String result = this.readInAllSequentially(id, new LazyFlatteningIterator<>(List.of(
            (Supplier<List<Language>>) /*is this fucking java can't determine type or fucking idea nags about nonexistent error?*/
                () -> List.of(language),
            () -> this.targetFallback.get(language),
            () -> this.defaultFallback,
            () -> List.of(this.native_)
        ).iterator()));
        if (result == null) return language.code()+":"+id;
        else return result;
    }
    
    private @Nullable String readInAllSequentially(String id, Iterator<Language> fallbacks) {
        while (fallbacks.hasNext()) {
            Language language = fallbacks.next();
            String result = this.readDirectly(id, language);
            if (result != null) return result;
        }
        return null;
    }
    
    private @Nullable String readDirectly(String id, Language language) {
        var languageMap = this.map.get(language);
        return languageMap.get(id);
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