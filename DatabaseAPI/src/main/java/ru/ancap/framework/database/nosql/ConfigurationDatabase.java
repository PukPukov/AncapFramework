package ru.ancap.framework.database.nosql;

import lombok.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.aware.Aware;
import ru.ancap.commons.aware.ContextAware;
import ru.ancap.commons.aware.InsecureContextHandle;
import ru.ancap.framework.resource.PluginResourceSource;
import ru.ancap.framework.resource.config.FileConfigurationPreparator;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@With(value = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString @EqualsAndHashCode
public class ConfigurationDatabase implements PathDatabase {

    private final FileConfiguration configuration;
    private final File file;
    private final String currentPath;
    private final boolean autoSave;
    private final long autoSavePeriod;
    
    private boolean timerStarted = false;
    private boolean updated = false;
    private Timer timer;
    private Executor modifyExecutor = Executors.newSingleThreadExecutor();
    private boolean isScheduled;
    private long lastSave;

    public static PathDatabase plugin(JavaPlugin plugin) {
        return ConfigurationDatabase.builder()
                .plugin(plugin)
                .build();
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
        protected long autoSavePeriod = 5L;
        
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
        
        public Builder autoSave(long period) {
            this.autoSave = true;
            this.autoSavePeriod = period;
            return this;
        }
        
        @SneakyThrows
        protected PathDatabase build() {
            return new ConfigurationDatabase(
                    this.config,
                    this.file,
                    "",
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
                FileConfiguration config = new PluginResourceSource<>(this.plugin, FileConfigurationPreparator.plain()).getResource(this.name);
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
        this.modifyExecutor.execute(() -> {
            this.configuration.set(this.attachedPath(path), object);
            this.performActionsAtModify();
        });
    }

    @NonBlocking
    @Override public void nullify() {
        this.modifyExecutor.execute(() -> {
            this.configuration.set(this.currentPath, null);
            this.performActionsAtModify();
        });
    }
    
    @NonBlocking
    @Override public void save() {
        this.modifyExecutor.execute(() -> {
            try {
                this.configuration.save(this.file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @ContextAware(handle = InsecureContextHandle.NO_HANDLE, awareOf = Aware.THREAD)
    private void scheduleSave() {
        if (ConfigurationDatabase.this.isScheduled) return;
        this.next(
            ConfigurationDatabase.this.autoSavePeriod - (System.currentTimeMillis() - ConfigurationDatabase.this.lastSave),
            new TimerTask() {
                @Override public void run() {
                    ConfigurationDatabase.this.modifyExecutor.execute(() -> {
                        ConfigurationDatabase.this.isScheduled = false;
                        ConfigurationDatabase.this.save();
                        ConfigurationDatabase.this.lastSave = System.currentTimeMillis();
                    });
                }
            }
        );
    }

    
    private void next(long waitTime, TimerTask timerTask) {
        if (waitTime <= 0) timerTask.run();
        else ConfigurationDatabase.this.timer.schedule(timerTask, waitTime);
    }

    private void performActionsAtModify() {
        if (this.autoSave) this.scheduleSave();
    }
    
    /* WRITE */

    @NonBlocking @Override public void delete(String path)                    { this.set(path, null);  }
    @NonBlocking @Override public void write(String path, double value)       { this.set(path, value); }
    @NonBlocking @Override public void write(String path, long value)         { this.set(path, value); }
    @NonBlocking @Override public void write(String path, String value)       { this.set(path, value); }
    @NonBlocking @Override public void write(String path, List<String> value) { this.set(path, value); }
    @NonBlocking @Override public void write(String path, ItemStack value)    { this.set(path, value); }
    @NonBlocking @Override public void write(String path, boolean value)      { this.set(path, value); }
    
    /* READ */

    @Override public @NotNull PathDatabase inner(String path) {
        return this.withCurrentPath(this.attachedPath(path));
    }

    @Override public @Nullable String readString(String path) {
        Object value = this.configuration.get(this.attachedPath(path));
        if (value == null) return null;
        if (!(value instanceof String)) throw new DifferentDatatypeException();
        return (String) value;
    }

    @Override public @Nullable ItemStack readItemStack(String path) {
        Object value = this.configuration.get(this.attachedPath(path));
        if (value == null) return null;
        if (!(value instanceof ItemStack)) throw new DifferentDatatypeException();
        return (ItemStack) value;
    }

    @Override public @Nullable Boolean readBoolean(String path) {
        Object value = this.configuration.get(this.attachedPath(path));
        if (value == null) return null;
        if (!(value instanceof Boolean)) throw new DifferentDatatypeException();
        return (Boolean) value;
    }

    @Override public @Nullable Long readInteger(String path) {
        Object value = this.configuration.get(this.attachedPath(path));
        if (value == null) return null;
        if (!(value instanceof Long)) throw new DifferentDatatypeException();
        return (Long) value;
    }
    
    @Override public @Nullable Double readNumber(String path) {
        Object value = this.configuration.get(this.attachedPath(path));
        if (value == null) return null;
        if (!(value instanceof Double)) throw new DifferentDatatypeException();
        return (Double) value;
    }
    
    @Override public @NotNull List<String> readStrings(String path) {
        List<String> strings = this.configuration.getStringList(this.attachedPath(path));
        if (strings.size() == 0) {
            var value = this.configuration.get(path);
            if (value instanceof String) strings = List.of((String) value);
        }
        return List.copyOf(strings);
    }

    @Override public @Nullable Set<String> readKeys(String path) {
        ConfigurationSection section = this.configuration.getConfigurationSection(this.attachedPath(path));
        if (section == null) return null;
        return section.getKeys(false);
    }

    @Override public boolean isSet(String path) {
        return this.configuration.isSet(this.attachedPath(path));
    }

    private String attachedPath(@NotNull String path) {
        return path.equals("") ? this.currentPath : this.currentPath+"."+path;
    }
    
}
