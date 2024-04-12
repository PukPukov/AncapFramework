package ru.ancap.framework.plugin.api.language.locale.loader;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import ru.ancap.commons.resource.ResourceSource;
import ru.ancap.framework.language.loader.YamlLocaleLoader;

import java.util.logging.Logger;

@AllArgsConstructor
public class LocaleLoader implements Runnable {

    private final Logger logger;
    private final ResourceSource<FileConfiguration> source;

    @Override
    public void run() {
        int index = 0;
        while (true) {
            FileConfiguration fileConfiguration = this.source.getResource("locale_"+index+".yml");
            if (fileConfiguration == null) break;
            new YamlLocaleLoader(fileConfiguration).run();
            this.logger.info("Loaded #"+index+" locale");
            index++;
        }
    }
}