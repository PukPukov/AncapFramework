package ru.ancap.framework.artifex.implementation.language.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@DatabaseTable
@Data @AllArgsConstructor @NoArgsConstructor
@ToString @EqualsAndHashCode
public class SpeakerModel {
    
    @DatabaseField(id = true)
    private String name;

    @DatabaseField
    private String languageCode;

}
