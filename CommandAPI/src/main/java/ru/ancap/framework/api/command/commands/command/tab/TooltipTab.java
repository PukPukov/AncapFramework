package ru.ancap.framework.api.command.commands.command.tab;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.Optional;

@AllArgsConstructor
public class TooltipTab implements TabCompletion {

    private final String completion;
    private final Component tooltip;

    @Override
    public String getCompletion() {
        return completion;
    }

    @Override
    public Optional<Component> getTooltipState() {
        return Optional.of(tooltip);
    }
}
