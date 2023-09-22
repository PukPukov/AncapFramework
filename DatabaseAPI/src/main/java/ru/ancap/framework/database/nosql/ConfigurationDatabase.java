package ru.ancap.framework.database.nosql;

import lombok.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.aware.Aware;
import ru.ancap.commons.aware.ContextAware;
import ru.ancap.commons.aware.InsecureContextHandle;
import ru.ancap.framework.database.nosql.exception.DifferentDatatypeException;
import ru.ancap.framework.database.nosql.exception.UnknownDatatypeException;
import ru.ancap.framework.resource.GuaranteedPluginResourceSource;
import ru.ancap.framework.resource.config.FileConfigurationPreparator;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString @EqualsAndHashCode
public class ConfigurationDatabase implements PathDatabase, AutoCloseable {

    private final FileConfiguration configuration;
    private final File              file;
    private final boolean           autoSave;
    private final long              autoSavePeriod;

    private final Timer           timer          = new Timer();
    private final ReadWriteLock   synchronizer   = new ReentrantReadWriteLock();
    private final ExecutorService modifyExecutor = Executors.newSingleThreadExecutor();
    private final AtomicBoolean   timerStarted   = new AtomicBoolean(false);
    private final AtomicBoolean   updated        = new AtomicBoolean(false);
    private final AtomicBoolean   isScheduled    = new AtomicBoolean(false);
    private final AtomicLong      lastSave       = new AtomicLong(0);
    private final AtomicBoolean   closed         = new AtomicBoolean(false);

    public static PathDatabase plugin(JavaPlugin plugin) {
        return ConfigurationDatabase.builder()
            .plugin(plugin).build();
    }

    public static PathDatabase file(File file) {
        return ConfigurationDatabase.builder()
            .file(file).build();
    }
    
    public static Builder builder() {
        return new ConfigurationDatabase.Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    public static class Builder {
        
        protected FileConfiguration config;
        protected File file;
        protected boolean autoSave = true;
        protected long autoSavePeriod = 5000L;
        
        public PluginBased plugin(JavaPlugin plugin) {
            return new PluginBased(this, plugin);
        }
        
        @SneakyThrows
        public FileBased file(File file) {
            return new FileBased(this, file);
        }
        
        public Builder manualSave() {
            this.autoSave = false;
            return this;
        }
        
        public Builder autoSave(long millis) {
            this.autoSave = true;
            this.autoSavePeriod = millis;
            return this;
        }
        
        public Builder autoSave(long time, TimeUnit timeUnit) {
            return this.autoSave(timeUnit.toMillis(time));
        }
        
        @SneakyThrows
        protected PathDatabase build() {
            return new ConfigurationDatabase(
                this.config,
                this.file,
                this.autoSave,
                this.autoSavePeriod
            );
        }
        
        @With(value = AccessLevel.PACKAGE)
        @AllArgsConstructor(access = AccessLevel.PACKAGE)
        public static class PluginBased {
            
            private Builder base;
            private JavaPlugin plugin;
            private String name;
            
            public PluginBased(Builder base, JavaPlugin plugin) {
                this.base = base;
                this.plugin = plugin;
                this.name = "database.yml";
            }
            
            public PluginBased name(String name) {
                this.name = name;
                return this;
            }
            
            public PathDatabase build() {
                FileConfiguration config = new GuaranteedPluginResourceSource<>(this.plugin, FileConfigurationPreparator.plain()).getResource(this.name);
                this.base.file = new File(this.plugin.getDataFolder(), this.name);
                this.base.config = config;
                return this.base.build();
            }
            
        }

        @With(value = AccessLevel.PACKAGE)
        @AllArgsConstructor(access = AccessLevel.PACKAGE)
        public static class FileBased {

            private Builder base;
            private File file;
            private InputStreamReader reader;
            
            public FileBased(Builder base, File file) throws FileNotFoundException {
                this(base, file, new FileReader(file));
            }
            
            public FileBased reader(InputStreamReader reader) {
                this.reader = reader;
                return this;
            }

            public PathDatabase build() {
                this.base.file = this.file;
                this.base.config = YamlConfiguration.loadConfiguration(this.reader);
                return this.base.build();
            }

        }
        
    }
    
    /* MODIFY */

    @NonBlocking
    private void set(String path, Object object) {
        this.synchronizer.writeLock().lock();
        this.configuration.set(path, object);
        this.synchronizer.writeLock().unlock();
        this.modifyExecutor.execute(this::performActionsAtModify);
    }

    @NonBlocking
    @Override public void nullify() {
        this.synchronizer.writeLock().lock();
        this.configuration.getKeys(false).forEach(key -> this.configuration.set(key, null));
        this.synchronizer.writeLock().unlock();
        this.modifyExecutor.execute(this::performActionsAtModify);
    }
    
    @NonBlocking
    @Override public void save() {
        this.synchronizer.writeLock().lock();
        try { this.configuration.save(this.file); }
        catch (IOException e) { throw new RuntimeException(e); }
        this.synchronizer.writeLock().unlock();
    }
    
    @ContextAware(handle = InsecureContextHandle.NO_HANDLE, awareOf = Aware.THREAD)
    private void scheduleSave() {
        if (ConfigurationDatabase.this.isScheduled.get()) return;
        this.isScheduled.set(true);
        this.scheduleTask(
            ConfigurationDatabase.this.autoSavePeriod - (System.currentTimeMillis() - ConfigurationDatabase.this.lastSave.get()),
            new TimerTask() {
                @Override public void run() {
                    if (ConfigurationDatabase.this.closed.get()) return;
                    ConfigurationDatabase.this.modifyExecutor.execute(() -> {
                        ConfigurationDatabase.this.isScheduled.set(false);
                        ConfigurationDatabase.this.save();
                        ConfigurationDatabase.this.lastSave.set(System.currentTimeMillis());
                    });
                }
            }
        );
    }
    
    private void scheduleTask(long waitTime, TimerTask timerTask) {
        if (waitTime <= 0) timerTask.run();
        else ConfigurationDatabase.this.timer.schedule(timerTask, waitTime);
    }

    private void performActionsAtModify() {
        if (this.autoSave) this.scheduleSave();
    }
    
    /* WRITE */

    @Override public <T> void write(@NonNull String path, @Nullable T value, SerializeWorker<T> worker) {
        if (value == null) {
            this.delete(path);
            return;
        }
        String dataTypeName = worker.dataType().getName();
        String serialized = worker.serialize(value);
        this.set(path, dataTypeName+":"+serialized);
    }

    @NonBlocking @Override public void delete(String path)                    { this.set(path, null);  }
    @NonBlocking @Override public void write(String path, double value)       { this.write(path, value, SerializeWorker.NUMBER); }
    @NonBlocking @Override public void write(String path, long value)         { this.write(path, value, SerializeWorker.INTEGER); }
    @NonBlocking @Override public void write(String path, String value)       { this.write(path, value, SerializeWorker.STRING); }
    @NonBlocking @Override public void write(String path, @NonNull List<String> value) { this.set(path, value); }
    @NonBlocking @Override public void write(String path, boolean value)      { this.write(path, value, SerializeWorker.BOOLEAN); }
    
    /* READ */
    
    @SneakyThrows
    @Override public <T> T read(@NonNull String path, SerializeWorker<T> serializeWorker) {
        Object fullDataObject = this.get(path);
        if (fullDataObject == null) return null;
        if (fullDataObject.getClass() != String.class) throw new DifferentDatatypeException(path, serializeWorker.dataType(), fullDataObject.getClass());
        String fullData = (String) fullDataObject; 
        String[] fullDataInArray = fullData.split(":");
        Class<?> dataType;
        try {
            dataType = Class.forName(fullDataInArray[0]);
        } catch (ClassNotFoundException exception) { 
            throw new UnknownDatatypeException(fullDataInArray[0]); 
        }
        
        if (dataType != serializeWorker.dataType()) throw new DifferentDatatypeException(path, serializeWorker.dataType(), dataType);
        
        String[] dataInArray = new String[fullDataInArray.length-1];
        System.arraycopy(fullDataInArray, 1, dataInArray, 0, dataInArray.length);
        String data = String.join(":", dataInArray);
        return serializeWorker.deserialize(data);
    }
    
    private Object get(String path) {
        this.synchronizer.readLock().lock();
        Object object = this.configuration.get(path);
        this.synchronizer.readLock().unlock();
        return object;
    }

    @Override public @NotNull PathDatabase inner(@NonNull String path) {
        if (path.isEmpty()) return this;
        return new ConfigurationDatabaseSection(this, path);
    }

    @Override public boolean isSet(String path) {
        this.synchronizer.readLock().lock();
        try {
            return this.configuration.get(path) != null;
        } finally {
            this.synchronizer.readLock().unlock();
        }
    }
    
    @Override public @NotNull Set<String> keys() {
        this.synchronizer.readLock().lock();
        Set<String> keys = this.configuration.getKeys(false);
        this.synchronizer.readLock().unlock();
        return keys;
    }
    
    public @NotNull Set<String> keys(String path) {
        this.synchronizer.readLock().lock();
        Object value = this.configuration.get(path);
        this.synchronizer.readLock().unlock();
        if (value == null) return Set.of();
        if (!(value instanceof ConfigurationSection section)) throw new DifferentDatatypeException(path, ConfigurationSection.class, value.getClass());
        return section.getKeys(false);
    }

    @Override public @Nullable String readString(String path) {
        return this.read(path, SerializeWorker.STRING);
    }

    @Override public @Nullable Boolean readBoolean(String path) {
        return this.read(path, SerializeWorker.BOOLEAN);
    }

    @Override public @Nullable Long readInteger(String path) {
        return this.read(path, SerializeWorker.INTEGER);
    }
    
    @Override public @Nullable Double readNumber(String path) {
        return this.read(path, SerializeWorker.NUMBER);
    }
    
    @Override public @NotNull List<String> readStrings(String path) {
        this.synchronizer.readLock().lock();
        List<String> strings = this.configuration.getStringList(path);
        this.synchronizer.readLock().unlock();
        if (strings.isEmpty()) {
            var value = this.get(path);
            //noinspection ConditionCoveredByFurtherCondition because null check should be explicit 
            if (value != null && value instanceof String string) strings = List.of(string);
        }
        return List.copyOf(strings);
    }
    
    @Override
    public void close() {
        this.save();
        this.closed.set(true);
        this.modifyExecutor.close();
    }
    
}
