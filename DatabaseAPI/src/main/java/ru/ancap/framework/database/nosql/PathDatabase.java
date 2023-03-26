package ru.ancap.framework.database.nosql;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public interface PathDatabase {
    
    static PathDatabase configuration(JavaPlugin plugin) {
        return ConfigurationDatabase.plugin(plugin);
    }

    void save();

    void nullify();
    void delete(String path);
    void write(String path, double value);
    void write(String path, long value);
    void write(String path, String value);
    void write(String path, List<String> value);
    void write(String path, ItemStack value);

    @NotNull PathDatabase inner(String path);

    @Nullable String       readString(String path);
    @Nullable ItemStack    readItemStack(String path);
    @Nullable Boolean      readBoolean(String path);
    @Nullable Long         readInteger(String path);
    @Nullable Double       readNumber(String path);
    @Nullable List<String> readStrings(String path);
    @Nullable Set<String>  readKeys(String path);

    default List<String> readStrings(String path, boolean nullIsEmpty) { 
        List<String> strings = this.readStrings(path);
        if (strings == null && nullIsEmpty) strings = List.of();
        return strings;
    }
    
    default Set<String> readKeys(String path, boolean nullIsEmpty) {
        Set<String> keys = this.readKeys(path);
        if (keys == null && nullIsEmpty) keys = Set.of();
        return keys;
    }

    boolean isSet(String path);
    
    default void add(String path, String value) {
        this.add(path, value, false);
    }
    
    default void add(String path, String value, boolean nullIsEmpty) {
        List<String> retrieved = this.readStrings(path, nullIsEmpty);
        if (retrieved == null) throw new IllegalStateException("Tried to add to null section");
        List<String> list = new ArrayList<>(retrieved);
        list.add(value);
        this.write(path, list);
    }
    
    default void remove(String path, String value) {
        this.remove(path, value, false);
    }

    default void remove(String path, String value, boolean nullIsEmpty) throws NoSuchElementException {
        List<String> retrieved = this.readStrings(path, nullIsEmpty);
        if (retrieved == null) throw new IllegalStateException("Tried to remove from null section");
        if (!this.contains(path, value)) throw new NoSuchElementException();
        List<String> list = new ArrayList<>(retrieved);
        list.remove(value);
        this.write(path, list);
    }
    
    default boolean contains(String path, String value) {
        return this.contains(path, value, false);
    }

    default boolean contains(String path, String value, boolean nullIsEmpty) {
        List<String> retrieved = this.readStrings(path, nullIsEmpty);
        if (retrieved == null) throw new IllegalStateException("Tried to get information about null section");
        return retrieved.stream()
            .anyMatch(s -> s.equals(value));
    }
}
