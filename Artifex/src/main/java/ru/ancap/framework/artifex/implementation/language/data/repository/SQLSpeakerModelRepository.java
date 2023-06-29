package ru.ancap.framework.artifex.implementation.language.data.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import lombok.*;
import ru.ancap.framework.database.sql.SQLDatabase;
import ru.ancap.framework.artifex.implementation.language.data.model.SpeakerModel;

import java.sql.SQLException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString @EqualsAndHashCode
public class SQLSpeakerModelRepository implements SpeakerModelRepository {

    private final SQLDatabase sql;
    private final Dao<SpeakerModel, String> data;
    
    public SQLSpeakerModelRepository(SQLDatabase sql) throws SQLException {
        this(sql, DaoManager.createDao(sql.orm(), SpeakerModel.class));
    }
    
    @SneakyThrows
    public SQLSpeakerModelRepository load() {
        TableUtils.createTableIfNotExists(this.sql.orm(), SpeakerModel.class);
        return this;
    }

    @Override
    @SneakyThrows
    public SpeakerModel read(String name) {
        return this.data.queryForId(name);
    }

    @Override
    @SneakyThrows
    public void create(SpeakerModel model) {
        this.data.create(model);
    }

    @Override
    @SneakyThrows
    public void update(SpeakerModel model) {
        this.data.update(model);
    }

}
