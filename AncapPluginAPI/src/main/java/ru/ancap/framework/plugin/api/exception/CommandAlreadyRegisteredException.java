package ru.ancap.framework.plugin.api.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandAlreadyRegisteredException extends RuntimeException {
    
    private final String command;
    
    @Override
    public String getMessage() {
        return this.command;
    }
    
}
