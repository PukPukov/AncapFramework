package ru.ancap.framework.api.command.commands.command.executor.conversation;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.command.commands.command.tab.TabCompletion;

import java.util.List;

public interface CommandLineSpeaker extends CommandSender {

    CommandSender getSender();

    void sendTabs(List<String> tabs);
    void sendDescriptionTab(String argumentTab);

    void sendDescriptionTab(TabCompletion completion);

    void sendTooltipTabs(List<TabCompletion> tabs);

    default void sendTooltip(Component tooltip) {
        throw new UnsupportedOperationException();
    }

    default void setLine(Component line) {
        throw new UnsupportedOperationException();
    }
}
