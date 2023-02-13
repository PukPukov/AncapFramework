package ru.ancap.framework.command.api.commands.object.tab;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.Optional;

@AllArgsConstructor
public class Tab implements TabCompletion {
    
    private final String completion;

    @Override
    public Optional<Component> tooltipState() {
        return Optional.empty();
    }
    
    @Override
    public String completion() {
        return this.completion;
    }
    
    
}
