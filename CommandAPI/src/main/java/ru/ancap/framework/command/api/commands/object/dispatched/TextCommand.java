package ru.ancap.framework.command.api.commands.object.dispatched;

import lombok.*;
import ru.ancap.framework.command.api.commands.object.dispatched.exception.NoNextArgumentException;
import ru.ancap.framework.command.api.syntax.CSCommand;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TextCommand implements CSCommand {

    @With private final List<String> args;
    @With private final TextCommand fullCommand;
    private final TextCommand originalCommand;

    public TextCommand(List<String> args) {
        this.args = List.copyOf(args);
        this.originalCommand = this;
        this.fullCommand = this;
    }
    
    public List<String> args() {
        return this.args;
    }
    
    public TextCommand full() {
        return this.fullCommand;
    }
    
    public TextCommand original() {
        return this.originalCommand;
    }

    @Override
    public TextCommand withoutArgument() {
        return this.withoutArguments(1);
    }

    @Override
    public TextCommand withoutArguments(int arguments) {
        this.nextArguments(arguments);
        return this.withArgs(this.args.subList(arguments, this.args.size()));
    }

    @Override
    public String consumeArgument() throws NoNextArgumentException {
        return this.nextArguments(1).get(0);
    }

    @Override
    public List<String> nextArguments(int arguments) {
        try {
            return this.args.subList(0, arguments - 1 + 1);
        } catch (IndexOutOfBoundsException e) {
            throw new NoNextArgumentException();
        }
    }

    public boolean isRaw() {
        return this.args.isEmpty();
    }

    @Override
    public List<String> arguments() {
        return this.args;
    }

    public String getFlattenedArgs() {
        return String.join(" ", this.args);
    }

    @Override
    public String toString() {
        return "TextCommand{" +
                "args=" + args +
                "original_args=" + originalCommand.args +
                "full_args=" + fullCommand.args +
                '}';
    }
}
