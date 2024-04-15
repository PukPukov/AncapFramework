package ru.ancap.framework.database.sql.registry;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.ancap.framework.database.sql.SQLDatabase;

/**
 * Ensure the Registry is fully initialized and restored if instantiated as a java object. 
 * Separate class is workaround for Java diamond operator not working with static methods.
 */
@RequiredArgsConstructor
public class RegistryInitialization<ID, SQL extends JavaConvertable<ID, JAVA>, JAVA extends SQLConvertable<ID, SQL>> {
    
    private final SQLDatabase database;
    private final Class<SQL> initializedClass;
    
    @SneakyThrows
    public Registry<ID, SQL, JAVA> run() {
        TableUtils.createTableIfNotExists(this.database.orm(), this.initializedClass);
        Dao<SQL, ID> dao = DaoManager.createDao(this.database.orm(), this.initializedClass);
        var registry = new Registry<>(dao);
        registry.restore();
        return registry;
    }
    
}