package ru.ancap.framework.command.api.commands.object.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.ancap.framework.command.api.commands.object.conversation.CommandLineSpeaker;
import ru.ancap.framework.command.api.syntax.CSCommand;

@AllArgsConstructor
@ToString @EqualsAndHashCode
@Accessors(fluent = true) @Getter
public final class CommandWrite implements CommandEvent {

    private final CommandLineSpeaker speaker;
    private final CSCommand arguments;

}
