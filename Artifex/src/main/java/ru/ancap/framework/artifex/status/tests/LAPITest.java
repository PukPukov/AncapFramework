package ru.ancap.framework.artifex.status.tests;

import ru.ancap.commons.exception.uewrapper.USupplier;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.language.loader.YamlLocaleLoader;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.status.test.AbstractTest;
import ru.ancap.framework.status.util.TestDomain;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LAPITest extends AbstractTest {
    
    public LAPITest(AncapPlugin plugin) {
        super(
            TestDomain.of(Artifex.class, "lapi"),
            new USupplier<>(() -> {
                String dummyPlayerID = "govnoed";
                String testSection = "test_"+ UUID.randomUUID(); // so it will not interfere with anything
                
                LAPI.setupLanguage(dummyPlayerID, Language.of("ru"));
                
                // test that nothing returned when nothing placed into LAPI
                assertEquals("ru:test", LAPI.localized("test", dummyPlayerID));
                
                // test basic retrieval
                File testFile = new File(plugin.getDataFolder(), "test.yml");
                Files.copy(plugin.getResource("test-locale-0.yml"), testFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                new YamlLocaleLoader(testSection, plugin.configuration("test.yml")).load();
                assertEquals("foo", LAPI.localized("test", dummyPlayerID));
                
                // test drop
                LAPI.drop(testSection);
                assertEquals("ru:test", LAPI.localized("test", dummyPlayerID));
                
                // test that reload after drop changes everything correctly
                Files.copy(plugin.getResource("test-locale-1.yml"), testFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                new YamlLocaleLoader(testSection, plugin.configuration("test.yml")).load();
                assertEquals("bar", LAPI.localized("test", dummyPlayerID));
                LAPI.drop(testSection);
                
                Files.delete(testFile.toPath());
                return TestResult.SUCCESS;
            })
        );
    }
    
}