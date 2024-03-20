package ru.ancap.framework.mccsyntax.bukkitadv.tab;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.kyori.adventure.text.Component;

import java.util.Optional;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class TooltipTab implements TabSuggestion {

    private final String completion;
    private final Component tooltip;

    @Override
    public String completion() {
        return this.completion;
    }

    @Override
    public Optional<Component> tooltipState() {
        return Optional.of(this.tooltip);
    }
    
}
