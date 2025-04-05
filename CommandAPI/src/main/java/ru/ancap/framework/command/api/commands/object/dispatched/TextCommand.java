package ru.ancap.framework.command.api.commands.object.dispatched;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true) @Getter
public class TextCommand implements LeveledCommand {

    @With private final List<String> args;
    @With private final TextCommand full;
    private final TextCommand original;

    public TextCommand(List<String> args) {
        this.args = List.copyOf(args);
        this.original = this;
        this.full = this;
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

    @SneakyThrows
    @Override
    public List<String> nextArguments(int arguments, Supplier<? extends Throwable> ifNo) {
        try {
            return this.args.subList(0, arguments);
        } catch (IndexOutOfBoundsException e) {
            throw ifNo.get();
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
                "args=" + this.args +
                "original_args=" + this.original.args +
                "full_args=" + this.full.args +
                '}';
    }
    
}