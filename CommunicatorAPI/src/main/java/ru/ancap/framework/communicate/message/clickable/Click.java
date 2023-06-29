package ru.ancap.framework.communicate.message.clickable;

import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
public class Click {
    
    private final CommandSender clicker;
    
    public CommandSender clicker() { return this.clicker; }
    
}
