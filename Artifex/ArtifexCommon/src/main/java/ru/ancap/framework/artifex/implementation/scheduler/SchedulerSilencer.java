package ru.ancap.framework.artifex.implementation.scheduler;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import lombok.*;
import ru.ancap.framework.database.sql.SQLDatabase;

import java.sql.SQLException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString @EqualsAndHashCode
public class SchedulerSilencer {
    
    private final SQLDatabase sql;
    private final Dao<SilencedTask, String> data;
    
    public SchedulerSilencer(SQLDatabase sql) throws SQLException {
        this(sql, DaoManager.createDao(sql.orm(), SilencedTask.class));
    }
    
    @SneakyThrows
    public SchedulerSilencer load() {
        TableUtils.createTableIfNotExists(this.sql.orm(), SilencedTask.class);
        return this;
    }
    
    public boolean isSilenced(String uuid) {
        SilencedTask task = this.get(uuid);
        return task == null || task.isSilenced();
    }
    
    @SneakyThrows
    public SilencedTask get(String uuid) {
        return this.data.queryForId(uuid);
    }
    
    @SneakyThrows
    public void upsert(SilencedTask task) {
        this.data.createOrUpdate(task);
    }
    
}
