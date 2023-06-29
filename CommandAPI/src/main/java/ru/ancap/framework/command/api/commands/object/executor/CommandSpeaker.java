package ru.ancap.framework.command.api.commands.object.executor;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.tab.TabBundle;
import ru.ancap.framework.command.api.commands.object.tab.TooltipTab;

import java.util.stream.Collectors;

/**
 * Next-gen CommandExecutor - больше не является callable, а значит, может быть полностью (двусторонне) асинхронным. Зачем это нужно?
 * Изначально, CommandExecutor, представляемый Bukkit'ом, имеет callable метод
 * {@snippet :
 *  List<String> onTabComplete();
 * }
 * Не сложно догадаться, что это пример абсолютного говнокода, особенно учитывая что этот метод вызывается в основном потоке
 * сервера - если, например, для получения таб комплитов надо обратиться к удалённой базе данных, до которой пинг 100, весь
 * сервер придётся остановить на 100 мс.
 */

public interface CommandSpeaker {

    void on(CommandWrite write);

    class Empty implements CommandSpeaker {

        public void on(CommandWrite write) {
        }
        
    }

    class Players implements CommandSpeaker {

        public void on(CommandWrite write) {
            write.speaker().sendTab(TabBundle.builder().tooltiped(
                Bukkit.getOnlinePlayers().stream().map(
                    player -> new TooltipTab(
                        player.getName(), 
                        Component.text(player.getHealth() + " §c♥")
                    )
                ).collect(Collectors.toList())
            ).build());
        }
        
    }

}
