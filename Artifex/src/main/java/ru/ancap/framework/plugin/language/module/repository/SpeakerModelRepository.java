package ru.ancap.framework.plugin.language.module.repository;

import ru.ancap.framework.plugin.language.module.model.SpeakerModel;

public interface SpeakerModelRepository {

    SpeakerModel read(String name);
    void create(SpeakerModel model);
    void update(SpeakerModel model);

}
