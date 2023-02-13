package ru.ancap.framework.database.nosql;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface PathDatabase {
    
    static PathDatabase configuration(JavaPlugin plugin) {
        return ConfigurationDatabase.plugin(plugin);
    }

    void save();

    void nullify();
    void write(String path, double value);
    void write(String path, long value);
    void write(String path, String value);
    void write(String path, List<String> value);
    void write(String path, ItemStack value);

    @NotNull PathDatabase inner(String path);

    @Nullable String getString(String path);
    @Nullable ItemStack getItemStack(String path);

    boolean getBoolean(String path);
    long getNumber(String path);
    double getDouble(String path);
    @NotNull List<String> getStrings(String path);
    @NotNull Set<String> getKeys(String path);

    boolean isSet(String path);
    
    void delete(String castle);
    
    default void add(String path, String value) {
        List<String> list = this.getStrings(path);
        list.add(value);
        this.write(path, list);
    }

    default void remove(String path, String value) {
        if (!this.contains(path, value)) throw new IllegalStateException();
        List<String> list = this.getStrings(path);
        list.remove(value);
        this.write(path, list);
    }

    default boolean contains(String path, String value) {
        return this.getStrings(path).stream().anyMatch(s -> s.equals(value));
    }
}
