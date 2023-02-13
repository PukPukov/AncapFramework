package ru.ancap.util.resource;

import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;

@AllArgsConstructor
public class PluginResourceSource implements ResourceSource {

    private final JavaPlugin plugin;
    private final boolean saveFiles;

    @Override
    public InputStream getResource(String fileName) {
        File file = new File(this.plugin.getDataFolder().getPath() + File.separator + fileName);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        InputStream stream = this.plugin.getResource(fileName);
        if (stream == null) {
            return null;
        }
        this.prepareFiles(file, stream);
        return plugin.getResource(fileName);
    }

    private void prepareFiles(File file, InputStream stream) {
        if (this.saveFiles) {
            this.createFile(file, stream);
        }
    }

    private void createFile(File file, InputStream stream) {
        try (stream) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            Files.copy(
                    stream,
                    file.toPath()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
