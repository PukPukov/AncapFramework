package ru.ancap.framework.communicate.message;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class WrapperText implements CallableText {
    
    @Delegate
    private final CallableText delegate;
    
}