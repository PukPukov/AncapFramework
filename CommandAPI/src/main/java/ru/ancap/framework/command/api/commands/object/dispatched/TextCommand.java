package ru.ancap.framework.command.api.commands.object.dispatched;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true) @Getter
@ToString @EqualsAndHashCode
public class TextCommand implements LeveledCommand {

    private final List<String> parts;
    private final List<String> originalParts;
    @With private final int currentPartIndex;
    
    public TextCommand(List<String> parts, List<String> originalParts) {
        if (parts.isEmpty()) throw new IllegalStateException("Command parts cant be empty");
        this.parts = parts;
        this.originalParts = originalParts;
        this.currentPartIndex = 0;
    }
    
    @Override
    @SneakyThrows
    public LCParseState step(Supplier<? extends Throwable> ifNo) {
        int nextPartIndex = this.currentPartIndex+1;
        if (nextPartIndex == this.parts.size()) throw ifNo.get();
        TextCommand nextCommand = this.withCurrentPartIndex(nextPartIndex);
        return new LCParseState(this.parts.get(nextPartIndex), nextCommand);
    }
    
}