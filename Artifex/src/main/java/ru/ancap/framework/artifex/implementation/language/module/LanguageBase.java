package ru.ancap.framework.artifex.implementation.language.module;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.language.LanguageSettings;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class LanguageBase implements LanguageSettings {

    private final LanguagesData database;
    private final Language defaultLanguage;

    @Override
    public Language getLanguage(@NotNull String playerName) {
        return Language.of(
                database.languageCode(
                        playerName,
                        defaultLanguage.code()
                )
        );
    }

    @Override
    public void setLanguage(@NotNull String playerName, @NotNull Language language) {
        database.setPlayerLanguage(playerName, language.code());
    }
}
