package ru.ancap.framework.plugin.api.language.locale.loader;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;
import ru.ancap.commons.resource.ResourceSource;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.loader.YamlLocaleLoader;

import java.util.logging.Logger;

@UtilityClass
public class LocaleLoader {
    
    public static LocaleHandle load(Logger logger, ResourceSource<FileConfiguration> source, String lapiSection, String versionFieldName) {
        int index = 0;
        while (true) {
            FileConfiguration fileConfiguration = source.getResource("locale_"+index+".yml");
            if (fileConfiguration == null) break;
            YamlLocaleLoader.load(fileConfiguration, lapiSection != null ? lapiSection : "global", versionFieldName);
            logger.info("Loaded #"+index+" locale");
            index++;
        }
        return lapiSection != null ? 
            () -> {
                LAPI.drop(lapiSection);
                LocaleLoader.load(logger, source, lapiSection, versionFieldName);
            } : 
            null;
    }
    
}