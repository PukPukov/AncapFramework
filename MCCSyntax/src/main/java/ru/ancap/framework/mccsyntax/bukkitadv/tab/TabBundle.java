package ru.ancap.framework.mccsyntax.bukkitadv.tab;

import lombok.*;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PACKAGE) @With
@ToString @EqualsAndHashCode
public class TabBundle {
    
    private final int startIndex;
    private final int endIndex;
    private final List<TabSuggestion> suggestions;
    
}
