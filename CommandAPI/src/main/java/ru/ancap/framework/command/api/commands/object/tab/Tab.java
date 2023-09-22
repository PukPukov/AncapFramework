package ru.ancap.framework.command.api.commands.object.tab;

import net.kyori.adventure.text.Component;

import java.util.Optional;

public record Tab(String completion) implements TabCompletion {

    @Override
    public Optional<Component> tooltipState() {
        return Optional.empty();
    }

}
