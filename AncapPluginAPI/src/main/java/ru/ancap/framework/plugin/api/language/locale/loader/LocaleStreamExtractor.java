package ru.ancap.framework.plugin.api.language.locale.loader;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class LocaleStreamExtractor {

    private final File folder;

    @SneakyThrows
    public List<InputStream> extract() {
        List<InputStream> inputStreams = new ArrayList<>();
        this.folder.mkdirs();
        for (File file : this.folder.listFiles()) inputStreams.add(new FileInputStream(file));
        return inputStreams;
    }
    
}
