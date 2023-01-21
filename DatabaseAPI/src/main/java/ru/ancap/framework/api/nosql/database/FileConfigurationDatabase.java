package ru.ancap.framework.api.nosql.database;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;

@With
@RequiredArgsConstructor
@AllArgsConstructor
public class FileConfigurationDatabase implements Database {

    private final FileConfiguration configuration;
    private final File file;
    private final String currentPath;
    private final boolean autoSave;
    private final long autoSavePeriod;
    
    private boolean timerStarted = false;
    private boolean updated = false;
    
    public FileConfigurationDatabase(JavaPlugin plugin) {
        this(plugin, "database.yml");
    }

    public FileConfigurationDatabase(JavaPlugin plugin, long autoSavePeriod) {
        this(plugin, "database.yml", autoSavePeriod);
    }
    
    public FileConfigurationDatabase(JavaPlugin plugin, String name) {
        this(new File(plugin.getDataFolder(), name));
    }
    
    public FileConfigurationDatabase(JavaPlugin plugin, String name, long autoSavePeriod) {
        this(new File(plugin.getDataFolder(), name), autoSavePeriod);
    }

    public FileConfigurationDatabase(File file) {
        this(YamlConfiguration.loadConfiguration(file), file);
    }

    public FileConfigurationDatabase(File file, long autoSavePeriod) {
        this(YamlConfiguration.loadConfiguration(file), file, autoSavePeriod);
    }

    public FileConfigurationDatabase(FileConfiguration configuration, File file) {
        this(configuration, file, "", false, 0);
    }

    public FileConfigurationDatabase(FileConfiguration configuration, File file, long autoSavePeriod) {
        this(configuration, file, "", false, autoSavePeriod);
    }

    @Override
    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nullify() {
        this.configuration.set(currentPath, null);
    }

    @Override
    public void write(String path, double value) {
        this.configuration.set(currentPath+"."+path, value);
    }

    @Override
    public @NotNull Database inner(String path) {
        return this.withCurrentPath(this.currentPath+"."+path);
    }

    @Override
    public void write(String path, long value) {
        this.set(currentPath+"."+path, value);
    }

    @Override
    public void write(String path, String value) {
        this.set(currentPath+"."+path, value);
    }

    @Override
    public void write(String path, List<String> value) {
        this.set(currentPath+"."+path, value);
    }

    @Override
    public void write(String path, ItemStack value) {
        this.set(currentPath+"."+path, value);
    }
    
    private void set(String path, Object object) {
        this.configuration.set(path, object);
        this.scheduleSave();
    }

    private void scheduleSave() {
        if (!this.autoSave) return;
        if (!this.timerStarted) new Thread(() -> {
            while (true) {
                if (this.updated) {
                    save();
                    this.updated = false;
                }
                LockSupport.parkUntil(this.autoSavePeriod*1000);
            }
        }).start();
        this.updated = true;
    }

    @Override
    public String getString(String path) {
        return this.configuration.getString(currentPath+"."+path);
    }

    @Override
    public @Nullable ItemStack getItemStack(String path) {
        return this.configuration.getItemStack(currentPath+"."+path);
    }

    @Override
    public boolean getBoolean(String path) {
        return this.configuration.getBoolean(currentPath+"."+path, false);
    }

    @Override
    public long getNumber(String path) {
        return this.configuration.getLong(currentPath+"."+path);
    }

    @Override
    public double getDouble(String path) {
        return this.configuration.getDouble(currentPath+"."+path);
    }

    @Override
    public @NotNull List<String> getStrings(String path) {
        return this.configuration.getStringList(currentPath+"."+path);
    }

    @Override
    public @NotNull Set<String> getKeys(String path) {
        ConfigurationSection section = this.configuration.getConfigurationSection(currentPath+"."+path);
        if (section == null) {
            return Set.of();
        }
        return section.getKeys(false);
    }

    @Override
    public boolean isSet(String path) {
        return this.configuration.isSet(currentPath+"."+path);
    }

    @Override
    public void delete(String path) {
        this.configuration.set(currentPath+"."+path, null);
    }
}
