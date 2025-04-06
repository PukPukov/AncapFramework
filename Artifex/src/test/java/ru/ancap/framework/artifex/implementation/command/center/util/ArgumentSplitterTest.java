package ru.ancap.framework.artifex.implementation.command.center.util;

import org.junit.jupiter.api.Test;
import ru.ancap.framework.command.api.commands.object.dispatched.Part;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentSplitterTest {
    
    // BASIC TESTS
    
    @Test
    void testBasicSplitting() {
        String command = "foo bar baz";
        List<String> expectedParts = List.of("foo", "bar", "baz");
        List<String> expectedOriginals = List.of("foo", "bar", "baz");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
    }
    
    @Test
    void testQuotedArguments() {
        String command = "foo \"bar baz\"";
        List<String> expectedParts = List.of("foo", "bar baz");
        List<String> expectedOriginals = List.of("foo", "\"bar baz\"");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
    }
    
    @Test
    void testEscapedQuotes() {
        String command = "foo \"bar\\\" baz\"";
        List<String> expectedParts = List.of("foo", "bar\" baz");
        List<String> expectedOriginals = List.of("foo", "\"bar\\\" baz\"");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
    }
    
    @Test
    void testEscapedBackslash() {
        String command = "foo \"bar\\\\\" baz";
        List<String> expectedParts = List.of("foo", "bar\\", "baz");
        List<String> expectedOriginals = List.of("foo", "\"bar\\\\\"", "baz");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
    }
    
    @Test
    void testComplexCommand() {
        String command = "cmd \"arg with spaces\" simplearg \"another \\\"escaped\\\" arg\"";
        List<String> expectedParts = List.of("cmd", "arg with spaces", "simplearg", "another \"escaped\" arg");
        List<String> expectedOriginals = List.of("cmd", "\"arg with spaces\"", "simplearg", "\"another \\\"escaped\\\" arg\"");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
    }
    
    @Test
    void testOnlyEscapedBackslash() {
        String command = "cmd \"arg with \\\"escaped\\\" backslashes\\\\\"";
        List<String> expectedParts = List.of("cmd", "arg with \"escaped\" backslashes\\");
        List<String> expectedOriginals = List.of("cmd", "\"arg with \\\"escaped\\\" backslashes\\\\\"");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
    }
    
    // HEAT
    @Test
    void testHeatBasic() {
        String command = "foo bar baz";
        List<String> expectedParts = List.of("foo", "bar", "baz");
        List<String> expectedOriginals = List.of("foo", "bar", "baz");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
        assertTrue(results.hot());
    }
    
    @Test
    void testHeatBasicReverse() {
        String command = "foo bar baz ";
        List<String> expectedParts = List.of("foo", "bar", "baz");
        List<String> expectedOriginals = List.of("foo", "bar", "baz");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
        assertFalse(results.hot());
    }
    
    @Test
    void testHeatComplex() {
        String command = "foo \"bar baz ";
        List<String> expectedParts = List.of("foo", "bar baz ");
        List<String> expectedOriginals = List.of("foo", "\"bar baz ");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
        assertTrue(results.hot());
    }
    
    @Test
    void testHeatComplexReverse() {
        String command = "foo \"bar baz \"";
        List<String> expectedParts = List.of("foo", "bar baz ");
        List<String> expectedOriginals = List.of("foo", "\"bar baz \"");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
        assertFalse(results.hot());
    }
    
    @Test
    void testHeatComplexReverseEscaped() {
        String command = "foo \"bar baz \\\"";
        List<String> expectedParts = List.of("foo", "bar baz \"");
        List<String> expectedOriginals = List.of("foo", "\"bar baz \\\"");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
        assertTrue(results.hot());
    }
    
    @Test
    void testHeatNonQuoteEscaped() {
        String command = "foo bar baz \\";
        List<String> expectedParts = List.of("foo", "bar", "baz", "");
        List<String> expectedOriginals = List.of("foo", "bar", "baz", "\\");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
        assertTrue(results.hot());
    }
    
    @Test
    void testHeatNonQuoteEscaped2() {
        String command = "foo bar baz \\\\\\\\";
        List<String> expectedParts = List.of("foo", "bar", "baz", "\\\\");
        List<String> expectedOriginals = List.of("foo", "bar", "baz", "\\\\\\\\");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
        assertTrue(results.hot());
    }
    
    @Test
    void testHeatNonQuoteEscaped3() {
        String command = "foo bar baz \\f";
        List<String> expectedParts = List.of("foo", "bar", "baz", "f");
        List<String> expectedOriginals = List.of("foo", "bar", "baz", "\\f");
        var results = ArgumentSplitter.split(command);
        compareParts(expectedParts, results);
        compareOriginals(expectedOriginals, results);
        assertTrue(results.hot());
    }
    
    // INDEXES
    
    @Test
    void testBasicSplittingIndexes() {
        String command = "foo bar baz";
        var results = ArgumentSplitter.split(command);
        compareIndexes(results, 0, 2, 4, 6, 8, 10);
    }
    
    @Test
    void testBasicSplittingIndexesMultipleSpaces() {
        String command = "foo    bar   baz";
        var results = ArgumentSplitter.split(command);
        compareIndexes(results, 0, 2, 7, 9, 13, 15);
    }
    
    @Test
    void testBasicSplittingIndexesStrangeSpaces() {
        String command = "  foo    bar   baz";
        var results = ArgumentSplitter.split(command);
        compareIndexes(results, 2, 4, 9, 11, 15, 17);
    }
    
    @Test
    void testQuotedArgumentsIndexes() {
        String command = "foo \"bar baz\"";
        var results = ArgumentSplitter.split(command);
        compareIndexes(results, 0, 2, 4, 12);
    }
    
    @Test
    void testEscapedBackslashIndexes() {
        String command = "foo \"bar\\\\\" baz";
        var results = ArgumentSplitter.split(command);
        compareIndexes(results, 0, 2, 4, 10, 12, 14);
    }
    
    // UTIL
    
    private void compareParts(List<String> expectedParts, ArgumentSplitter.SplitResult results) {
        List<String> actualParts = results.parts().stream().map(Part::main).toList();
        assertEquals(expectedParts, actualParts);
    }
    
    private void compareOriginals(List<String> expectedOriginals, ArgumentSplitter.SplitResult results) {
        for (int i = 0; i < expectedOriginals.size(); i++) {
            var expectedPart = expectedOriginals.get(i);
            assertEquals(expectedPart, results.parts().get(i).original());
        }
    }
    
    
    private void compareIndexes(ArgumentSplitter.SplitResult results, Integer... indexes) {
        List<Integer> expectedIndexes = Arrays.asList(indexes);
        List<Integer> actualIndexes = results.parts().stream()
            .flatMap(part -> Stream.of(part.beginIndexInclusive(), part.endIndexInclusive()))
            .toList();
        assertEquals(expectedIndexes, actualIndexes);
    }
    
}