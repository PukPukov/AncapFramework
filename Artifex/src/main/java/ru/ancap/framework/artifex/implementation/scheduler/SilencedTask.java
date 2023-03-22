package ru.ancap.framework.artifex.implementation.scheduler;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

@DatabaseTable
@Data @AllArgsConstructor @NoArgsConstructor
@ToString @EqualsAndHashCode
public class SilencedTask {
    
    @DatabaseField(id = true)
    private String taskUuid;
    
    @DatabaseField
    private boolean silenced;
    
    @DatabaseField
    private String pluginName;
    
}
