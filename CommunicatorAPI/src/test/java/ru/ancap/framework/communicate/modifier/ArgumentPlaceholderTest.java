package ru.ancap.framework.communicate.modifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ancap.framework.communicate.message.Message;

public class ArgumentPlaceholderTest {
    
    @Test
    public void test() {
        Modifier isOne = new ArgumentPlaceholder("is one", argument -> {
            if (argument.equals("1")) return new Message("true");
            else return new Message("false");
        });
        
        Modifier simple = new ArgumentPlaceholder("simple", Message::new);
        
        Assertions.assertEquals("true",  isOne.apply("{IS_ONE:1}", ""));
        Assertions.assertEquals("false", isOne.apply("{IS_ONE:0}", ""));
        
        Assertions.assertEquals("example true example",  isOne.apply("example {IS_ONE:1} example", ""));
        Assertions.assertEquals("example false example", isOne.apply("example {IS_ONE:0} example", ""));
        
        Assertions.assertEquals("{SOMETHING_OTHER:value} true example",  isOne.apply("{SOMETHING_OTHER:value} {IS_ONE:1} example", ""));
        Assertions.assertEquals("{SOMETHING_OTHER:value} false example", isOne.apply("{SOMETHING_OTHER:value} {IS_ONE:0} example", ""));
        
        Assertions.assertEquals("one:two example", simple.apply("{SIMPLE:one:two} example", ""));
        Assertions.assertEquals("one:two:::: example", simple.apply("{SIMPLE:one:two::::} example", ""));
        
        Assertions.assertEquals("one:two example one:two example", simple.apply("{SIMPLE:one:two} example {SIMPLE:one:two} example", ""));
        Assertions.assertEquals("one:two:::: example one:two:::: example", simple.apply("{SIMPLE:one:two::::} example {SIMPLE:one:two::::} example", ""));
        
        Assertions.assertEquals("one:two example one:two example one:two example", simple.apply("{SIMPLE:one:two} example {SIMPLE:one:two} example {SIMPLE:one:two} example", ""));
        Assertions.assertEquals("one:two:::: example one:two:::: example one:two:::: example", simple.apply("{SIMPLE:one:two::::} example {SIMPLE:one:two::::} example {SIMPLE:one:two::::} example", ""));
        
        Assertions.assertEquals("{ true example",  isOne.apply("{ {IS_ONE:1} example", ""));
        Assertions.assertEquals("{}{ {SOMETHING_OTHER:value} {{{ {dgd {g false example", isOne.apply("{}{ {SOMETHING_OTHER:value} {{{ {dgd {g {IS_ONE:0} example", ""));
        
        // Test nested placeholders
        Assertions.assertEquals("nested {inside} example", simple.apply("{SIMPLE:nested {inside}} example", ""));
        Assertions.assertEquals("nested {inside} {another} example", simple.apply("{SIMPLE:nested {inside}} {SIMPLE:{another}} example", ""));
        Assertions.assertEquals("nested {inside {nested}} example", simple.apply("{SIMPLE:nested {inside {nested}}} example", ""));
        Assertions.assertEquals("nested {inside {nested}} {more} example", simple.apply("{SIMPLE:nested {inside {nested}}} {SIMPLE:{more}} example", ""));
    }
    
}