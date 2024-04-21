package ru.ancap.framework.artifex.implementation.language.module;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;
import ru.ancap.framework.database.sql.registry.Registry;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.language.LanguageSettings;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class LanguageBase implements LanguageSettings {

    private final Registry<String, SpeakerModel, SpeakerModel> speakerRegistry;

    @Override
    public Language getLanguage(@NonNull String playerName) {
        return Language.of(this.speakerRegistry.read(playerName).orElseThrow().getLanguageCode());
    }
    
    @Override
    public void updateLanguage(@NonNull String playerName, @NonNull Language language) {
        var speakerOptional = this.speakerRegistry.read(playerName);
        SpeakerModel speaker = speakerOptional.orElseThrow();
        speaker.setLanguageCode(language.code());
        this.speakerRegistry.update(playerName, speaker);
    }
    
}