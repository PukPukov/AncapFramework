package ru.ancap.framework.database.nosql;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ConfigurationDatabaseSection implements PathDatabase {
    
    private final ConfigurationDatabase owner;
    private final String currentPath;
    
    @Override
    public void save() {
        this.owner.save();
    }

    @Override
    public void nullify() {
        this.owner.delete(this.currentPath);
    }

    @Override
    public void delete(String path) {
        this.owner.delete(this.attachedPath(path));
    }

    @Override
    public <T> void write(String path, T value, SerializeWorker<T> serializeWorker) {
        this.owner.write(this.attachedPath(path), value, serializeWorker);
    }

    @Override
    public void write(String path, String value) {
        this.owner.write(this.attachedPath(path), value);
    }

    @Override
    public void write(String path, boolean value) {
        this.owner.write(this.attachedPath(path), value);
    }

    @Override
    public void write(String path, double value) {
        this.owner.write(this.attachedPath(path), value);
    }

    @Override
    public void write(String path, long value) {
        this.owner.write(this.attachedPath(path), value);
    }

    @Override
    public void write(String path, List<String> value) {
        this.owner.write(this.attachedPath(path), value);
    }

    @Override
    public @NotNull PathDatabase inner(String path) {
        return this.owner.inner(this.attachedPath(path));
    }

    @Override
    public boolean isSet(String path) {
        return this.owner.isSet(this.attachedPath(path));
    }

    @Override
    public @NotNull Set<String> keys() {
        return this.owner.keys(this.currentPath);
    }

    @Override
    public <T> @Nullable T read(String path, SerializeWorker<T> serializeWorker) {
        return this.owner.read(this.attachedPath(path), serializeWorker);
    }

    @Override
    public @Nullable String readString(String path) {
        return this.owner.readString(this.attachedPath(path));
    }

    @Override
    public @Nullable Boolean readBoolean(String path) {
        return this.owner.readBoolean(this.attachedPath(path));
    }

    @Override
    public @Nullable Long readInteger(String path) {
        return this.owner.readInteger(this.attachedPath(path));
    }

    @Override
    public @Nullable Double readNumber(String path) {
        return this.owner.readNumber(this.attachedPath(path));
    }

    @Override
    public @Nullable List<String> readStrings(String path) {
        return this.owner.readStrings(this.attachedPath(path));
    }

    public String attachedPath(@NotNull String path) {
        return path.isEmpty() ? this.currentPath : this.currentPath+"."+path;
    }
    
}
