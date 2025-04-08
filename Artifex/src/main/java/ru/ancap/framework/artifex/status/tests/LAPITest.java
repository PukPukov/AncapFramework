package ru.ancap.framework.artifex.status.tests;

import lombok.SneakyThrows;
import ru.ancap.commons.exception.uewrapper.USupplier;
import ru.ancap.framework.artifex.configuration.ArtifexConfig;
import ru.ancap.framework.artifex.implementation.language.input.LAPIInitialLanguageInstaller;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.loader.YamlLocaleLoader;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.status.test.AbstractTest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LAPITest extends AbstractTest {
    
    public LAPITest(AncapPlugin plugin, LAPIInitialLanguageInstaller languageInstaller) {
        super(
            "lapi",
            new USupplier<>(() -> {
                final String localeForm = "test-locale-%NAME%.yml";
                final String filename = "test-file.yml";
                final String language = "test_lang";
                final String localeId = "test-locale-id";
                final String player = "govnoed";
                final String versionFieldName = "version";
                final String lapiSection = "test_"+ UUID.randomUUID(); // so it will not interfere with anything
                
                languageInstaller.prepareSpeaker(player, () -> language);
                LAPI.updateLanguage(player, Language.of(language));
                
                // check that nothing returned when nothing placed into LAPI
                assertEquals(language+":"+localeId, LAPI.localized(localeId, player));
                
                // check basic retrieval
                File file = new File(plugin.getDataFolder(), filename);
                Files.copy(plugin.getResource(locale(localeForm, "0")), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                YamlLocaleLoader.load(plugin.configuration(filename), lapiSection, versionFieldName);
                assertEquals("foo", LAPI.localized(localeId, player));
                
                // check drop
                LAPI.drop(lapiSection);
                assertEquals(language+":"+localeId, LAPI.localized(localeId, player));
                
                // check that reload after drop changes everything correctly
                Files.copy(plugin.getResource(locale(localeForm, "1")), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                YamlLocaleLoader.load(plugin.configuration(filename), lapiSection, versionFieldName);
                assertEquals("bar", LAPI.localized(localeId, player));
                LAPI.drop(lapiSection); 
                
                // check fallback to targeted
                Files.copy(plugin.getResource(locale(localeForm, "fallback-to-targeted")), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                YamlLocaleLoader.load(plugin.configuration(filename), lapiSection, versionFieldName);
                assertEquals("fell-to-targeted", LAPI.localized(localeId, player));
                LAPI.drop(lapiSection);
                
                // check fallback to default 
                replaceTextAndWriteToFile(
                    plugin.getResource(locale(localeForm, "fallback-to-default")),
                    file,
                    "%DEFAULT%",
                    ArtifexConfig.loaded().defaultFallback().getFirst().code()
                );
                YamlLocaleLoader.load(plugin.configuration(filename), lapiSection, versionFieldName);
                assertEquals("fell-to-default", LAPI.localized(localeId, player));
                LAPI.drop(lapiSection);
                
                // check fallback to native
                replaceTextAndWriteToFile(
                    plugin.getResource(locale(localeForm, "fallback-to-native")), file, "%NATIVE%",
                    ArtifexConfig.loaded().nativeLanguage().code()
                );
                YamlLocaleLoader.load(plugin.configuration(filename), lapiSection, versionFieldName);
                assertEquals("fell-to-native", LAPI.localized(localeId, player));
                LAPI.drop(lapiSection);
                
                // exit
                // LAPI.drop(lapiSection); — don't needed because already all subs make this on end
                Files.delete(file.toPath());
                return TestResult.SUCCESS;
            })
        );
    }
    
    private static String locale(String form, String name) {
        return form.replace("%NAME%", name);
    }
    
    @SneakyThrows
    private static void replaceTextAndWriteToFile(InputStream inputStream, File output, String target, String replacement) {
        Files.write(output.toPath(), List.of(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)); 
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String modifiedLine = line.replace(target, replacement);
                writer.write(modifiedLine);
                writer.newLine();
            }
        }
    }
    
}