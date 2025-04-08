package ru.ancap.framework.command.api.commands.object.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.ancap.framework.command.api.commands.object.conversation.CommandLineSpeaker;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;

@AllArgsConstructor
@ToString @EqualsAndHashCode
@Accessors(fluent = true) @Getter
public class CommandWrite {

    private final CommandLineSpeaker speaker;
    private final CommandSource source;
    private final LeveledCommand line;

}