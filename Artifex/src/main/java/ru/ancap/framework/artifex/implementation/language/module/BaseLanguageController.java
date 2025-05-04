package ru.ancap.framework.artifex.implementation.language.module;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;
import ru.ancap.framework.database.sql.registry.Repository;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.language.LanguageController;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class BaseLanguageController implements LanguageController {

    private final Repository<String, SpeakerModel, SpeakerModel> speakerRepository;

    @Override
    public Language getLanguage(@NonNull String playerName) {
        return Language.of(this.speakerRepository.read(playerName).orElseThrow().getLanguageCode());
    }
    
    @Override
    public void updateLanguage(@NonNull String playerName, @NonNull Language language) {
        SpeakerModel speaker = this.speakerRepository.read(playerName).orElseThrow();
        speaker.setLanguageCode(language.code());
        this.speakerRepository.update(playerName, speaker);
    }
    
}