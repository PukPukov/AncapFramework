package ru.ancap.framework.database.nosql;

import lombok.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.util.resource.PluginResourceSource;

import java.io.*;
import java.util.List;
import java.util.Set;

@With(value = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ConfigurationDatabase implements PathDatabase {

    private final FileConfiguration configuration;
    private final File file;
    private final String currentPath;
    private final boolean autoSave;
    private final long autoSavePeriod;
    
    private boolean timerStarted = false;
    private boolean updated = false;
    
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
        
        protected InputStreamReader reader;
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
                    YamlConfiguration.loadConfiguration(this.reader),
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
                InputStreamReader reader = new InputStreamReader(new PluginResourceSource(this.plugin, true).getResource(this.name));
                this.base.file = new File(this.plugin.getDataFolder(), this.name);
                this.base.reader = reader;
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
                this.base.reader = this.reader;
                return this.base.build();
            }

        }
        
    }

    @Override
    public void save() {
        try {
            this.configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nullify() {
        this.configuration.set(this.currentPath, null);
        this.scheduleSave();
    }

    @Override
    public void delete(String path) {
        this.set(path, null);
    }

    @Override
    public void write(String path, double value) {
        this.set(path, value);
    }

    @Override
    public void write(String path, long value) {
        this.set(path, value);
    }

    @Override
    public void write(String path, String value) {
        this.set(path, value);
    }

    @Override
    public void write(String path, List<String> value) {
        this.set(path, value);
    }

    @Override
    public void write(String path, ItemStack value) {
        this.set(path, value);
    }
    
    private void set(String path, Object object) {
        this.configuration.set(this.currentPath+"."+path, object);
        this.scheduleSave();
    }

    private void scheduleSave() {
        if (!this.autoSave) return;
        if (!this.timerStarted) new Thread(() -> {
            this.timerStarted = true;
            while (true) {
                if (this.updated) {
                    save();
                    this.updated = false;
                }
                try {
                    Thread.sleep(this.autoSavePeriod*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        this.updated = true;
    }

    @Override
    public @NotNull PathDatabase inner(String path) {
        return this.withCurrentPath(this.currentPath+"."+path);
    }

    @Override
    public String getString(String path) {
        return this.configuration.getString(this.currentPath+"."+path);
    }

    @Override
    public @Nullable ItemStack getItemStack(String path) {
        return this.configuration.getItemStack(this.currentPath+"."+path);
    }

    @Override
    public boolean getBoolean(String path) {
        return this.configuration.getBoolean(this.currentPath+"."+path, false);
    }

    @Override
    public long getNumber(String path) {
        return this.configuration.getLong(this.currentPath+"."+path);
    }

    @Override
    public double getDouble(String path) {
        return this.configuration.getDouble(this.currentPath+"."+path);
    }

    @Override
    public @NotNull List<String> getStrings(String path) {
        List<String> strings = this.configuration.getStringList(this.currentPath+"."+path);
        if (strings.size() == 0) {
            String string = this.configuration.getString(path);
            if (string != null) strings = List.of(string);
            else strings = List.of();
        }
        return strings;
    }

    @Override
    public @NotNull Set<String> getKeys(String path) {
        ConfigurationSection section = this.configuration.getConfigurationSection(this.currentPath+"."+path);
        if (section == null) {
            return Set.of();
        }
        return section.getKeys(false);
    }

    @Override
    public boolean isSet(String path) {
        return this.configuration.isSet(this.currentPath+"."+path);
    }
}
