package ru.ancap.framework.language.language;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString @EqualsAndHashCode
public class PlayersLanguageSettings implements LanguageSettings {

    private final Map<String, Language> map;
    private final Language defaultLanguage;

    public PlayersLanguageSettings(Language defaultLanguage) {
        this.map = new HashMap<>();
        this.defaultLanguage = defaultLanguage;
    }

    @Override
    public Language getLanguage(@NonNull String playerID) {
        return this.map.getOrDefault(playerID, defaultLanguage);
    }

    @Override
    public void setLanguage(@NonNull String playerID, @NonNull Language language) {
        this.map.put(playerID, language);
    }
}
