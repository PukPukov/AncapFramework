package ru.ancap.framework.command.api.commands.object.conversation;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import ru.ancap.framework.command.api.commands.object.tab.TabBundle;

import java.util.List;

public interface CommandLineSpeaker {
    
    @ApiStatus.Internal
    int transactionId();
    void sendTab(TabBundle tab);
    
    default void sendTab(List<String> tabs) {
        this.sendTab(TabBundle.builder()
                .raw(tabs)
                .build()
        );
    }
    
    default void sendTooltip(Component tooltip) {
        throw new UnsupportedOperationException();
    }
    
    default void setLine(Component line) {
        throw new UnsupportedOperationException();
    }
    
}