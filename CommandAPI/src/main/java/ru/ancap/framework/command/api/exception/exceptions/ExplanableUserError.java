package ru.ancap.framework.command.api.exception.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.ancap.framework.communicate.message.CallableMessage;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class ExplanableUserError extends UserError {

    private final CallableMessage explanation;

}
