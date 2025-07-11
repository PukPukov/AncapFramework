package ru.ancap.framework.artifex.status.tests;

import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.status.test.AbstractTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationTest extends AbstractTest {
    
    public ConfigurationTest(AncapPlugin plugin) {
        super(
            "configuration",
            () -> {
                var configuration = plugin.configuration();
                assertEquals("foo", configuration.getString("test.string"));
                assertEquals(420, configuration.getInt("test.integer"));
                assertEquals(true, configuration.getBoolean("test.section.boolean"));
                return TestResult.SUCCESS;
            }
        );
    }
    
}