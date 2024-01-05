package ru.ancap.framework.communicate.modifier;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Triple;
import ru.ancap.commons.parse.EscapingBuffer;
import ru.ancap.framework.communicate.message.CallableMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@ToString @EqualsAndHashCode
public class ArgumentPlaceholder implements Modifier {
    
    private static final PlaceholderFinder finder = new PlaceholderFinder();
    
    private final String name;
    private final Function<String, CallableMessage> to;

    public ArgumentPlaceholder(String name, Function<String, CallableMessage> to) {
        this.name = ArgumentPlaceholder.formalName(name);
        this.to = to;
    }

    private static String formalName(String name) {
        name = name.toUpperCase();
        name = name.replaceAll("[\\s-]" /* spaces and dashes*/ , "_");
        return name;
    }

    @Override
    public String apply(String base, String receiverId) {
        StringBuilder buffer = new StringBuilder(base);
        List<PlaceholderFinder.Match> result = ArgumentPlaceholder.finder.find(base.toCharArray());

        List<Triple<Integer, Integer, String>> replacements = new ArrayList<>();

        for (PlaceholderFinder.Match match : result) {
            if (!match.name().equals(this.name)) continue;
            String replacement = this.to.apply(match.argument()).call(receiverId);
            replacements.add(Triple.of(match.start(), match.end() + 1, replacement));
        }

        for (int i = replacements.size() - 1; i >= 0; i--) {
            Triple<Integer, Integer, String> replacement = replacements.get(i);
            buffer.replace(replacement.getLeft(), replacement.getMiddle(), replacement.getRight());
        }

        return buffer.toString();
    }

    private static class PlaceholderFinder {

        private List<Match> find(char[] string) {
            List<Match> matches = new ArrayList<>();
            
            char opening = '{';
            char closing = '}';
            char argumentDelimiter = ':';
            
            FindState finding = FindState.FINDING_OPENING;
            EscapingBuffer escapingBuffer = new EscapingBuffer();
            
            int start = -1;
            int separator = -1;

            for (int index = 0; index < string.length; index++) {
                escapingBuffer.step();
                if (escapingBuffer.currentlyEscaped()) continue;
                char char_ = string[index];
                if (char_ == '\\') { escapingBuffer.escapeNext(); continue; }
                
                switch (finding) {
                    case FINDING_OPENING -> {
                        if (char_ != opening) continue;
                        start = index;
                        finding = FindState.FINDING_ARGUMENT_DELIMITER;
                    }
                    case FINDING_ARGUMENT_DELIMITER -> {
                        if (char_ == opening) start = index;
                        if (char_ != argumentDelimiter) continue;
                        separator = index;
                        finding = FindState.FINDING_CLOSING;
                    }
                    case FINDING_CLOSING -> {
                        if (char_ != closing) continue;
                        String name = new String(string, start + 1, separator - start - 1);
                        String argument = new String(string, separator + 1, index - separator - 1);
                        matches.add(new Match(start, index, name, argument));
                        finding = FindState.FINDING_OPENING;
                    }
                }
            }

            return matches;
        }

        private record Match(int start, int end, String name, String argument) {}

        private enum FindState {
            
            FINDING_OPENING,
            FINDING_ARGUMENT_DELIMITER,
            FINDING_CLOSING
            
        }
    }
    
}
