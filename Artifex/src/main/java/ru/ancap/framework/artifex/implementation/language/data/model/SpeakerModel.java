package ru.ancap.framework.artifex.implementation.language.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import ru.ancap.framework.database.sql.registry.AmbiguousSelfConvertableConversionAttemptException;
import ru.ancap.framework.database.sql.registry.JavaConvertable;
import ru.ancap.framework.database.sql.registry.SQLConvertable;

@DatabaseTable
@Data @AllArgsConstructor @NoArgsConstructor
@ToString @EqualsAndHashCode
public class SpeakerModel implements SQLConvertable<String, SpeakerModel>, JavaConvertable<String, SpeakerModel> {
    
    @DatabaseField(id = true)
    private String name;

    @DatabaseField
    private String languageCode;
    
    @Override
    public String id() {
        return this.name;
    }
    
    @Override
    public SpeakerModel toJava() {
        return this;
    }
    
    @Override
    public SpeakerModel toSQL(String id) {
        if (!(this.name.equals(id))) throw new AmbiguousSelfConvertableConversionAttemptException(this.name, id, SpeakerModel.class);
        return this;
    }
    
}