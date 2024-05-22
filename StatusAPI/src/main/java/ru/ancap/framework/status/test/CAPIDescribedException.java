package ru.ancap.framework.status.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.ancap.framework.communicate.message.CallableMessage;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class CAPIDescribedException extends RuntimeException {
    
    private final CallableMessage message;
    
}