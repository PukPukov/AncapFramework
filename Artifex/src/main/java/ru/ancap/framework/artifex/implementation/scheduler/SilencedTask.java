package ru.ancap.framework.artifex.implementation.scheduler;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DatabaseTable
@Data @AllArgsConstructor @NoArgsConstructor
public class SilencedTask {
    
    @DatabaseField(id = true)
    private String taskUuid;
    
    @DatabaseField
    private boolean silenced;
    
    @DatabaseField
    private String pluginName;
    
}
