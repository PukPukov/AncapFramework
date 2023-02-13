package ru.ancap.framework.command.api.commands.object.tab;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.Optional;

@AllArgsConstructor
public class TooltipTab implements TabCompletion {

    private final String completion;
    private final Component tooltip;

    @Override
    public String completion() {
        return completion;
    }

    @Override
    public Optional<Component> tooltipState() {
        return Optional.of(tooltip);
    }
}
