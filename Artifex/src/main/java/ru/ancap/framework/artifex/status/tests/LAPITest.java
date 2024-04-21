package ru.ancap.framework.artifex.status.tests;

import lombok.SneakyThrows;
import ru.ancap.commons.exception.uewrapper.USupplier;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.artifex.configuration.ArtifexConfig;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.loader.YamlLocaleLoader;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.status.test.AbstractTest;
import ru.ancap.framework.status.util.TestDomain;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LAPITest extends AbstractTest {
    
    public LAPITest(AncapPlugin plugin) {
        super(
            TestDomain.of(Artifex.class, "lapi"),
            new USupplier<>(() -> {
                final String localeForm = "test-locale-%NAME%.yml";
                final String file = "test-file.yml";
                final String language = "test_lang";
                final String localeId = "test-locale-id";
                final String player = "govnoed";
                final String lapiSection = "test_"+ UUID.randomUUID(); // so it will not interfere with anything
                
                LAPI.setupLanguage(player, Language.of(language));
                
                // test that nothing returned when nothing placed into LAPI
                assertEquals(language+":"+localeId, LAPI.localized(localeId, player));
                
                // test basic retrieval
                File testFile = new File(plugin.getDataFolder(), file);
                Files.copy(plugin.getResource(locale(localeForm, "0")), testFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                new YamlLocaleLoader(lapiSection, plugin.configuration(file)).load();
                assertEquals("foo", LAPI.localized(localeId, player));
                
                // test drop
                LAPI.drop(lapiSection);
                assertEquals(language+":"+localeId, LAPI.localized(localeId, player));
                
                // test that reload after drop changes everything correctly
                Files.copy(plugin.getResource(locale(localeForm, "1")), testFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                new YamlLocaleLoader(lapiSection, plugin.configuration(file)).load();
                assertEquals("bar", LAPI.localized(localeId, player));
                LAPI.drop(lapiSection); 
                
                // test fallback to targeted
                Files.copy(plugin.getResource(locale(localeForm, "fallback-to-targeted")), testFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                new YamlLocaleLoader(lapiSection, plugin.configuration(file)).load();
                assertEquals("fell-to-target", LAPI.localized(localeId, player));
                LAPI.drop(lapiSection);
                
                // test fallback to default 
                replaceTextAndWriteToFile(
                    plugin.getResource(locale(localeForm, "fallback-to-default")),
                    testFile,
                    "%DEFAULT%",
                    ArtifexConfig.loaded().defaultFallback().getFirst().code()
                );
                new YamlLocaleLoader(lapiSection, plugin.configuration(file)).load();
                assertEquals("fell-to-default", LAPI.localized(localeId, player));
                LAPI.drop(lapiSection);
                
                // test fallback to native
                replaceTextAndWriteToFile(
                    plugin.getResource(locale(localeForm, "fallback-to-default")), testFile, "%DEFAULT%",
                    ArtifexConfig.loaded().defaultFallback().getFirst().code()
                );
                new YamlLocaleLoader(lapiSection, plugin.configuration(file)).load();
                assertEquals("fell-to-default", LAPI.localized(localeId, player));
                LAPI.drop(lapiSection);
                
                // fallback to native
                replaceTextAndWriteToFile(
                    plugin.getResource(locale(localeForm, "fallback-to-native")), testFile, "%NATIVE%",
                    ArtifexConfig.loaded().nativeLanguage().code()
                );
                new YamlLocaleLoader(lapiSection, plugin.configuration(file)).load();
                assertEquals("fell-to-native", LAPI.localized(localeId, player));
                LAPI.drop(lapiSection);
                
                // exit tests
                // LAPI.drop(lapiSection); — don't needed because already all subtests make this on end
                Files.delete(testFile.toPath());
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