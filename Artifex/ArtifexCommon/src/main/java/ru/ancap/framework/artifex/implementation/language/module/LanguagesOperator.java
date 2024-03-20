package ru.ancap.framework.artifex.implementation.language.module;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;
import ru.ancap.framework.artifex.implementation.language.data.repository.SpeakerModelRepository;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class LanguagesOperator implements LanguagesData {

    private final SpeakerModelRepository repository;

    @Override
    @NonNull
    public String languageCode(String speakerId, String defaultCode) {
        SpeakerModel model = repository.read(speakerId);
        if (model == null) {
            return defaultCode;
        }
        return model.getLanguageCode();
    }

    @Override
    public void setPlayerLanguage(String speakerId, String languageCode) {
        repository.update(new SpeakerModel(speakerId, languageCode));
    }
}
