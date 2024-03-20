package ru.ancap.framework.command.api.commands.object.conversation;

import net.kyori.adventure.text.Component;
import ru.ancap.framework.mccsyntax.bukkitadv.tab.TabSuggestion;

import java.util.List;

public interface CommandLineSpeaker {

    CommandSource source();
    
    TabSendStatement tab(List<TabSuggestion> completions);

    default void tooltipLine(Component tooltip) {
        // throw new UnsupportedOperationException();
    }
    
    // Util
    
    default TabSendStatement tab_str(List<String> tabs) {
        return this.tab(tabs.stream()
            .map(TabSuggestion::simple).toList());
    }
    
}
