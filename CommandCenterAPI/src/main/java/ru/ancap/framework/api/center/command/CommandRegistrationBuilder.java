package ru.ancap.framework.api.center.command;

import org.jetbrains.annotations.CheckReturnValue;

import java.util.List;

public interface CommandRegistrationBuilder {
    
    @CheckReturnValue CommandRegistrationBuilder handler(CommandEventHandler handler);
    @CheckReturnValue CommandRegistrationBuilder aliases(List<String> aliases);
    @CheckReturnValue CommandRegistrationBuilder addAliases(List<String> aliases);
    
    void exec();
    
}