package ru.ancap.framework.plugin.language.module;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.language.Language;
import ru.ancap.framework.api.language.LanguageSettings;

@AllArgsConstructor
public class LanguageBase implements LanguageSettings {

    private final LanguagesData database;
    private final Language defaultLanguage;

    @Override
    public Language getLanguage(@NotNull String playerName) {
        return Language.of(
                database.languageCode(
                        playerName,
                        defaultLanguage.getCode()
                )
        );
    }

    @Override
    public void setLanguage(@NotNull String playerName, @NotNull Language language) {
        database.setPlayerLanguage(playerName, language.getCode());
    }
}
