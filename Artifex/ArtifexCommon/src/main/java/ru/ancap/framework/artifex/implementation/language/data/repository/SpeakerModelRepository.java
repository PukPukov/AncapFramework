package ru.ancap.framework.artifex.implementation.language.data.repository;

import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;

public interface SpeakerModelRepository {

    SpeakerModel read(String name);
    void create(SpeakerModel model);
    void update(SpeakerModel model);

}
