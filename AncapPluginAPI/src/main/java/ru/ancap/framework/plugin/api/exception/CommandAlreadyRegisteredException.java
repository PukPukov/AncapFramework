package ru.ancap.framework.plugin.api.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@ToString @EqualsAndHashCode(callSuper = true)
public class CommandAlreadyRegisteredException extends RuntimeException {
    
    private final String command;
    
    @Override
    public String getMessage() {
        return this.command;
    }
    
}