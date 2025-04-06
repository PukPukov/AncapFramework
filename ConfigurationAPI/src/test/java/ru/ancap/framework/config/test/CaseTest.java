package ru.ancap.framework.config.test;

import org.junit.jupiter.api.Test;
import ru.ancap.framework.configuration.Case;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CaseTest {
    
    @Test
    public void testCase() {
        assertEquals("example-text-in-this-case", Case.camelToKebab("exampleTextInThisCase"));
        assertEquals("lowercase", Case.camelToKebab("lowercase"));
        assertEquals("uppercase", Case.camelToKebab("UPPERCASE"));
        assertEquals("example123in-this-case", Case.camelToKebab("example123InThisCase"));
        assertEquals("example!text@in-this#case", Case.camelToKebab("example!Text@InThis#Case"));
        assertEquals("foo-bar", Case.camelToKebab("FooBar"));
    }
    
}