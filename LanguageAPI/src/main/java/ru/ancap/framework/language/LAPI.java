package ru.ancap.framework.language;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;
import ru.ancap.commons.ImplementationRequired;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.language.LanguageSettings;
import ru.ancap.framework.language.language.LocalisationStatistic;
import ru.ancap.framework.language.locale.Locales;

import java.util.List;
import java.util.Set;

@ImplementationRequired
public class LAPI {

    private static Locales locales;
    private static LanguageSettings settings;

    private LAPI() {}
    
    @Blocking
    public static String localeOf(String userId) {
        return LAPI.settings.getLanguage(userId).code();
    }

    /**
     * Load a locale into the base
     *
     * @deprecated see localized() + no sectioning
     */
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public static void loadLocale(String id, String localized, Language language) {
        LAPI.loadLocale("general", id, localized, language);
    }
    
    /**
     * @deprecated see localized()
     */
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public static void loadLocale(String section, String id, String localized, Language language) {
        LAPI.locales.loadLocale(section, id, localized, language);
    }
    
    /**
     * @deprecated see localized()
     */
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public static void drop(String section) {
        LAPI.locales.drop(section);
    }

    /**
     * It sets up the LAPI
     *
     * @param locales The Locales object that you created in the previous step.
     * @param settings The LanguageSettings object that you created in the previous step.
     */
    @ApiStatus.Internal // since 1.7
    public static void setup(Locales locales, LanguageSettings settings) {
        LAPI.locales = locales;
        LAPI.settings = settings;
    }

    @ApiStatus.Internal // since 1.7
    public static void setupLanguage(String playerID, Language language) {
        settings.setLanguage(playerID, language);
    }

    /**
     * It returns the localized string for the given id, using the language of the player with the given playerID.
     * <p>
     * Please note that a remote database can be accessed to get a localized string.
     * It is recommended to run the code for sending a message to the player in a separate thread!
     *
     * @param id The id of the string you want to localize.
     * @param playerID The player's ID.
     * @return The localized string.
     * @deprecated since 1.7 concept LAPI should be considered, first of all, as a source of information,
     * what locale player has. API and its implementation take the full control under it, leaving everything
     * else to integration modules. AncapFramework offers fancy preset of these modules, and, of course, raw
     * string module to make good old localization for strings. That decision was made because not only raw
     * strings can be different in different languages, but, for example, images with text. This method will
     * be removed in 1.7 release with a pack of other backward-compatibility breaks, functionality of it
     * will be transferred to StringLocales. 
     */
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public static String localized(String id, String playerID) {
        return locales.localized(id, settings.getLanguage(playerID));
    }
    
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public static String localized(String id, Language language) {
        return locales.localized(id, language);
    }

    /**
     * @deprecated Special case of the problem, described in {@link LAPI#localized(String, String)}. Not only string can change
     * with language, but, for example, string list. Impossible to add everything to one class, and, of course, bad idea to try to do it.
     */
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public static List<String> localizedList(String id, String playerID) {
        return List.of(localized(id, playerID).split("\n"));
    }
    
    public static Set<Language> allLanguages() {
        return locales.allLanguages();
    }
    
    public static LocalisationStatistic statistic(Language language) {
        return locales.statistic(language);
    }
    
    public static Set<String> allKeys(Language language) {
        return locales.allKeys(language);
    }
    
}