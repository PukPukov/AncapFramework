package ru.ancap.framework.plugin.api.language.locale.loader;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class LocaleStreamExtractor {

    private final File folder;

    public List<InputStream> extract() {
        List<InputStream> inputStreams = new ArrayList<>();
        folder.mkdirs();
        for (File file : this.folder.listFiles()) {
            try {
                inputStreams.add(
                        new FileInputStream(file)
                );
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return inputStreams;
    }
}
