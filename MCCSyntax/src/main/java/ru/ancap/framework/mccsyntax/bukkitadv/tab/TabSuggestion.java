package ru.ancap.framework.mccsyntax.bukkitadv.tab;

import net.kyori.adventure.text.Component;

import java.util.Optional;

public interface TabSuggestion {

    static TabSuggestion simple(String str) {
        return new Tab(str);
    }
    
    static TabSuggestion tooltip(String str, Component tooltip) {
        return new TooltipTab(str, tooltip);
    }

    String completion();
    Optional<Component> tooltipState();

}
