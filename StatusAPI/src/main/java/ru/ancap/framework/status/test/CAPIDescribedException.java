package ru.ancap.framework.status.test;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.ancap.framework.communicate.message.CallableText;

@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
@ToString @EqualsAndHashCode(callSuper = true)
public class CAPIDescribedException extends RuntimeException {
    
    private final CallableText message;
    
}