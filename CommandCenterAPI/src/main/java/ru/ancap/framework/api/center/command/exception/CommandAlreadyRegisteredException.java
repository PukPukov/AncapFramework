package ru.ancap.framework.api.center.command.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandAlreadyRegisteredException extends RuntimeException {
    
    private final String id;
    
    @Override
    public String getMessage() {
        return this.id;
    }
    
}
