package ru.ancap.framework.language.locale;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.language.LocalisationStatistic;

import java.util.Set;

@ApiStatus.Internal
public interface Locales {

    /**
     * Loads localized message to base.
     *
     * @param id        of loading message
     * @param localized localized message
     * @param language  language of localization
     */
    void loadLocale(@NotNull String section, @NotNull String id, @NotNull String localized, @NotNull Language language);

    void drop(String section);

    /**
     * Returns localized message from base for inserted language.
     * @param id of needed message
     * @param language of needed localization
     */
    @NotNull
    String localized(@NotNull String id, @NotNull Language language);
    
    Set<Language> allLanguages();
    LocalisationStatistic statistic(Language language);
    Set<String> allKeys(Language language);
    
}