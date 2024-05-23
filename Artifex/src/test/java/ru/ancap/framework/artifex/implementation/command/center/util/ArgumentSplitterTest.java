package ru.ancap.framework.artifex.implementation.command.center.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArgumentSplitterTest {
    
    @Test
    void testBasicSplitting() {
        String command = "foo bar baz";
        List<String> expected = List.of("foo", "bar", "baz");
        assertEquals(expected, ArgumentSplitter.split(command));
    }
    
    @Test
    void testQuotedArguments() {
        String command = "foo \"bar baz\"";
        List<String> expected = List.of("foo", "bar baz");
        assertEquals(expected, ArgumentSplitter.split(command));
    }
    
    @Test
    void testEscapedQuotes() {
        String command = "foo \"bar\\\" baz\"";
        List<String> expected = List.of("foo", "bar\" baz");
        assertEquals(expected, ArgumentSplitter.split(command));
    }
    
    @Test
    void testEscapedBackslash() {
        String command = "foo \"bar\\\\\" baz";
        List<String> expected = List.of("foo", "bar\\", "baz");
        assertEquals(expected, ArgumentSplitter.split(command));
    }
    
    @Test
    void testComplexCommand() {
        String command = "cmd \"arg with spaces\" simplearg \"another \\\"escaped\\\" arg\"";
        List<String> expected = List.of("cmd", "arg with spaces", "simplearg", "another \"escaped\" arg");
        assertEquals(expected, ArgumentSplitter.split(command));
    }
    
    @Test
    void testOnlyEscapedBackslash() {
        String command = "cmd \"arg with \\\"escaped\\\" backslashes\\\\\"";
        List<String> expected = List.of("cmd", "arg with \"escaped\" backslashes\\");
        assertEquals(expected, ArgumentSplitter.split(command));
    }
    
}