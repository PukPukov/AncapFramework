package ru.ancap.framework.api.command.commands.command.dispatched;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
public final class InlineTextCommand implements LeveledCommand {

    private final String line;

    private List<ParseLexem> parsed;
    private TextCommand completedCommand;

    public InlineTextCommand(String line) {
        if (line.startsWith("/")) {
            line = line.substring(1);
        }
        this.line = line;
    }

    @Delegate
    public TextCommand getCompletedCommand() {
        if (completedCommand == null) {
            List<ParseLexem> lexems = new ArrayList<>(this.parsed());
            int lastIndex = lexems.size() - 1;
            if (!lexems.get(lastIndex).finished) {
                lexems.remove(lastIndex);
            }
            this.completedCommand = new TextCommand(lexems
                    .stream()
                    .map(ParseLexem::getLexem)
                    .collect(Collectors.toList())
            );
        }
        return completedCommand;
    }

    public int hotArgumentStart() {
        if (!this.hot()) {
            return this.line.length();
        }
        int index = this.line.length()-1;
        while (line.charAt(index) != ' ') {
            index--;
        }
        return index+1;
    }

    public boolean hot() {
        List<ParseLexem> lexems = this.parsed();
        return !lexems.get(lexems.size() - 1).finished;
    }

    public String getHotArgument() {
        List<ParseLexem> lexems = this.parsed();
        if (!this.hot()) {
            return "";
        } else {
            return lexems.get(lexems.size() - 1).lexem;
        }
    }

    private List<ParseLexem> parsed() {
        if (this.parsed == null) {
            boolean lastFinished = this.line.endsWith(" ");
            List<ParseLexem> lexems = new ArrayList<>();
            List<String> strings = List.of(line.split(" "));
            for (int i = 0; i < strings.size(); i++) {
                if (i == strings.size() - 1) {
                    lexems.add(new ParseLexem(strings.get(i), lastFinished));
                    break;
                }
                lexems.add(new ParseLexem(strings.get(i), true));
            }
            this.parsed = lexems;
        }
        return List.copyOf(this.parsed);
    }

    public int hotArgumentEnd() {
        return this.line.length();
    }


    @Data
    @AllArgsConstructor
    private static class ParseLexem {
        private final String lexem;
        private final boolean finished;
    }
}
