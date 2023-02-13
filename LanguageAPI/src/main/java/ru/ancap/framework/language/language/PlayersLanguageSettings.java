package ru.ancap.framework.language.language;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlayersLanguageSettings implements LanguageSettings {

    private final Map<String, Language> map;
    private final Language defaultLanguage;

    public PlayersLanguageSettings(Language defaultLanguage) {
        this.map = new HashMap<>();
        this.defaultLanguage = defaultLanguage;
    }

    @Override
    public Language getLanguage(@NotNull String playerID) {
        return this.map.getOrDefault(playerID, defaultLanguage);
    }

    @Override
    public void setLanguage(@NotNull String playerID, @NotNull Language language) {
        this.map.put(playerID, language);
    }
}
