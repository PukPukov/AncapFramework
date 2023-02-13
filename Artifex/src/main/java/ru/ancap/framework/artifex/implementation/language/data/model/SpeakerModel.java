package ru.ancap.framework.artifex.implementation.language.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DatabaseTable
@Data @AllArgsConstructor @NoArgsConstructor
public class SpeakerModel {
    
    @DatabaseField(id = true)
    private String name;

    @DatabaseField
    private String languageCode;

}
