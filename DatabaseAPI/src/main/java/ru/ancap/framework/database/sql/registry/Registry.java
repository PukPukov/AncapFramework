package ru.ancap.framework.database.sql.registry;

import com.j256.ormlite.dao.Dao;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Experimental // there is many questions how to implement/name this properly
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Registry<ID, SQL extends JavaConvertable<ID, JAVA>, JAVA extends SQLConvertable<ID, SQL>> {
    
    private final Dao<SQL, ID> cold;
    private final Map<ID, JAVA> hot = new ConcurrentHashMap<>();
    
    @SneakyThrows
    public void restore() {
        for (JavaConvertable<ID, JAVA> sql : this.cold.queryForAll()) {
            this.hot.put(sql.id(), sql.toJava());
        }
    }
    
    public Optional<JAVA> read(ID id) {
        return Optional.ofNullable(this.hot.get(id));
    }
    
    public Map<ID, JAVA> all() {
        return Collections.unmodifiableMap(this.hot);
    }
    
    /**
     * Suitable for cases when a java object in registry is mutable and already updated. Blazing-fast speed. Does not update hot storage, does not create if not exist.
     */
    @SneakyThrows
    public void update(ID id, JAVA object) {
        this.cold.update(object.toSQL(id));
    }
    
    /**
     * Makes full upsert to hot and cold storages. In a lot of cases {@code Registry.update(ID)} should be used instead of this.
     */
    @SneakyThrows
    public void save(ID id, JAVA object) {
        this.hot.put(id, object);
        this.cold.createOrUpdate(object.toSQL(id));
    }
    
    @SneakyThrows
    public void delete(ID id) {
        this.hot.remove(id);
        this.cold.deleteById(id);
    }
    
    
}