package ru.ancap.framework.command.api.commands.object.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.object.conversation.CommandLineSpeaker;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class CommandWrite {

    private final CommandLineSpeaker speaker;
    private final LeveledCommand line;

    public CommandLineSpeaker speaker() {
        return this.speaker;
    }

    public LeveledCommand line() {
        return this.line;
    }

}
