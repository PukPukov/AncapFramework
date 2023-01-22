package ru.ancap.framework.api.command.commands.command.tab;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;

import java.util.Optional;

@AllArgsConstructor
@Data
public class Tab implements TabCompletion {
    
    private final String completion;

    @Override
    public Optional<Component> getTooltipState() {
        return Optional.empty();
    }
}
