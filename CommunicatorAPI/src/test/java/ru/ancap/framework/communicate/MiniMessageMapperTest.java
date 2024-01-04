package ru.ancap.framework.communicate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiniMessageMapperTest {

    @Test
    public void mapLegacy() {
        assertEquals("foo<reset><blue>bar", MiniMessageMapper.mapLegacy("foo§9bar"));
        assertEquals("foo<reset><blue>bar", MiniMessageMapper.mapLegacy("foo&9bar"));
    }

}
