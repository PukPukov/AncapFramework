package ru.ancap.framework.artifex.implementation.language.module;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.language.LanguageSettings;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class LanguageBase implements LanguageSettings {

    private final LanguagesData database;
    private final Language defaultLanguage;

    @Override
    public Language getLanguage(@NonNull String playerName) {
        return Language.of(this.database.languageCode(
            playerName,
            this.defaultLanguage.code()
        ));
    }

    @Override
    public void setLanguage(@NonNull String playerName, @NonNull Language language) {
        this.database.setPlayerLanguage(playerName, language.code());
    }
}
