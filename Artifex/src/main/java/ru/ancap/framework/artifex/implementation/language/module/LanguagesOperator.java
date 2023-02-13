package ru.ancap.framework.artifex.implementation.language.module;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.artifex.implementation.language.data.repository.SpeakerModelRepository;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;

@AllArgsConstructor
public class LanguagesOperator implements LanguagesData {

    private final SpeakerModelRepository repository;

    @Override
    @NotNull
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
