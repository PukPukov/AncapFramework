package ru.ancap.framework.command.api.commands.object.tab;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.Optional;

@AllArgsConstructor
public class OptionalTab implements TabCompletion {
    
    private final String completion;
    private final Optional<Component> tooltip;
    
    @Override
    public String completion() {
        return this.completion;
    }

    @Override
    public Optional<Component> tooltipState() {
        return this.tooltip;
    }
}
