package ru.ancap.framework.mccsyntax.bukkitadv.tab;

import net.kyori.adventure.text.Component;

import java.util.Optional;

public record Tab(String completion) implements TabSuggestion {

    @Override
    public Optional<Component> tooltipState() {
        return Optional.empty();
    }

}
