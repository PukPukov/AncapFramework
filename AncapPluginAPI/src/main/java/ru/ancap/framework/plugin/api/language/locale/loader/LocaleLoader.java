package ru.ancap.framework.plugin.api.language.locale.loader;

import lombok.AllArgsConstructor;
import ru.ancap.framework.language.loader.YamlLocaleLoader;
import ru.ancap.util.resource.ResourceSource;
import ru.ancap.framework.plugin.api.configuration.StreamConfig;

import java.io.InputStream;
import java.util.logging.Logger;

@AllArgsConstructor
public class LocaleLoader implements Runnable{

    private final Logger logger;
    private final ResourceSource source;

    @Override
    public void run() {
        int index = 0;
        while (true) {
            InputStream stream = this.source.getResource("locale_"+index+".yml");
            if (stream == null) {
                break;
            }
            new YamlLocaleLoader(
                    new StreamConfig(
                            stream
                    )
            ).run();
            this.logger.info("Loaded #"+index+" locale");
            index++;
        }
    }
}
