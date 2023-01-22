package ru.ancap.framework.plugin.command.api.implementation.catcher;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTabComplete;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ancap.framework.api.command.commands.command.dispatched.InlineTextCommand;
import ru.ancap.framework.api.command.commands.command.executor.conversation.CommandLineSpeaker;
import ru.ancap.framework.api.command.commands.command.tab.Tab;
import ru.ancap.framework.api.command.commands.command.tab.TabCompletion;

import java.util.List;
import java.util.stream.Collectors;

public class PacketLineSpeaker implements CommandLineSpeaker {

    private final int transactionID;
    @Delegate
    @Getter
    private final CommandSender sender;
    private final Player player;
    private final InlineTextCommand command;

    public PacketLineSpeaker(int transactionID, InlineTextCommand command, Player player) {
        this.transactionID = transactionID;
        this.player = player;
        this.sender = player;
        this.command = command;
    }

    @Override
    public void sendTabs(@NotNull List<String> tabs) {
        this.sendTooltipTabs(
                tabs.stream().map(string -> (TabCompletion) new Tab(string)).collect(Collectors.toList()),
                true
        );
    }
    
    @Override
    public void sendDescriptionTab(String tab) {
        this.sendDescriptionTab(new Tab(tab));
    }

    @Override
    public void sendDescriptionTab(TabCompletion completion) {
        this.sendTooltipTabs(List.of(completion), false);
    }

    @Override
    public void sendTooltipTabs(List<TabCompletion> tabs) {
        this.sendTooltipTabs(tabs, true);
    }
    
    private void sendTooltipTabs(List<TabCompletion> tabs, boolean filter) {
        if (filter) {
            tabs = tabs.stream()
                    .filter(s -> s.getCompletion().startsWith(command.getHotArgument()))
                    .collect(Collectors.toList());
        }
        this.sendTab(
                tabs.stream()
                        .map(tabCompletion -> {
                            WrapperPlayServerTabComplete.CommandMatch[] match = new WrapperPlayServerTabComplete.CommandMatch[1];
                            tabCompletion.getTooltipState().ifPresentOrElse(
                                    component -> match[0] = new WrapperPlayServerTabComplete.CommandMatch(
                                            tabCompletion.getCompletion(),
                                            component
                                    ),
                                    () -> match[0] = new WrapperPlayServerTabComplete.CommandMatch(
                                            tabCompletion.getCompletion()
                                    )
                            );
                            return match[0];
                        })
                        .collect(Collectors.toList())
        );
    }

    private void sendTab(List<WrapperPlayServerTabComplete.CommandMatch> matches) {
        if (matches.size() == 0) return;
        WrapperPlayServerTabComplete complete = new WrapperPlayServerTabComplete(
                transactionID,
                // Здесь я добавляю +1 к каждому значению, потому что до этого был убран из строки первый символ (слэш), но клиент всё ещё принимает значения
                // пакетов как со слэшем. Это костыль, но его фикс довольно сложный, пока что пусть будет так, потом возможно переделаю внутреннюю структуру
                new WrapperPlayServerTabComplete.CommandRange(command.hotArgumentStart()+1, command.hotArgumentEnd()+1),
                matches
        );
        PacketEvents.getAPI().getPlayerManager().sendPacketSilently(player, complete);
    }
    
    
}
