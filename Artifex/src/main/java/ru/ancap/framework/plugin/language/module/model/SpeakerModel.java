package ru.ancap.framework.plugin.language.module.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpeakerModel {
    
    private final String name;
    private final String languageCode;

}
