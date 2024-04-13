package ru.ancap.framework.plugin.api.language.locale.loader;

import lombok.AllArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.ApiStatus;
import ru.ancap.commons.resource.ResourceSource;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.loader.YamlLocaleLoader;

import java.util.logging.Logger;

@AllArgsConstructor
public class LocaleLoader implements Runnable {

    private final Logger logger;
    private final ResourceSource<FileConfiguration> source;
    private final String lapiSection;
    
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public LocaleLoader(Logger logger, ResourceSource<FileConfiguration> source) {
        this(logger, source, null);
    }

    @Override
    @Deprecated(forRemoval = true)
    @ApiStatus.ScheduledForRemoval(inVersion = "1.7")
    public void run() {
        this.load();
    }
    
    public LocaleHandle load() {
        int index = 0;
        while (true) {
            FileConfiguration fileConfiguration = this.source.getResource("locale_"+index+".yml");
            if (fileConfiguration == null) break;
            new YamlLocaleLoader(this.lapiSection != null ? this.lapiSection : "global", fileConfiguration).load();
            this.logger.info("Loaded #"+index+" locale");
            index++;
        }
        return this.lapiSection != null ?
            () -> {
                LAPI.drop(LocaleLoader.this.lapiSection);
                LocaleLoader.this.load();
            } :
            null;
    }
    
}