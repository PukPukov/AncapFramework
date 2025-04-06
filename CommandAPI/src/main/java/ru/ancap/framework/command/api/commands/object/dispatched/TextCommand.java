package ru.ancap.framework.command.api.commands.object.dispatched;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true) @Getter
@ToString @EqualsAndHashCode
public class TextCommand implements LeveledCommand {

    private final List<Part> completedParts;
    private final Optional<Part> hotPart;
    @With private final int currentPartIndex;
    
    public TextCommand(List<Part> completedParts, Optional<Part> hotPart) {
        if (completedParts.isEmpty()) throw new IllegalStateException("Command parts cant be empty");
        this.completedParts = completedParts;
        this.hotPart = hotPart;
        this.currentPartIndex = 0;
    }
    
    @Override
    public List<Part> parts() {
        return this.completedParts;
    }
    
    @Override
    @SneakyThrows
    public LCParseState step(Supplier<? extends Throwable> ifNo) {
        int nextPartIndex = this.currentPartIndex+1;
        if (nextPartIndex == this.completedParts.size()) throw ifNo.get();
        TextCommand nextCommand = this.withCurrentPartIndex(nextPartIndex);
        return new LCParseState(this.completedParts.get(nextPartIndex), nextCommand);
    }
    
}