package ru.ancap.framework.plugin.api.commands;

public interface CommandEventHandler {
    
    void on(BukkitCommandDispatch dispatch);
    void on(BukkitCommandWrite write);
    
}
