package ru.ancap.framework.api.center.command;

public interface CommandEventHandler {
    
    void on(BukkitCommandDispatch dispatch);
    void on(BukkitCommandWrite write);
    
}