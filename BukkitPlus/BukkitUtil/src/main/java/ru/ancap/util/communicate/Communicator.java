package ru.ancap.util.communicate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.ancap.util.AudienceProvider;
import ru.ancap.util.Replacement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public class Communicator {
    
    private final static List<BiFunction<String, CommandSender, String>> modifiers = new ArrayList<>(List.of(
            (string, sender) -> ChatColor.translateAlternateColorCodes('&', string),
            (string, sender) -> MiniMessageMapper.mapLegacy(string)
    ));
    private final CommandSender receiver;

    public void send(String basic, Replacement... replacements) {
        final String[] operatedArr = {basic};
        modifiers.forEach(modifier -> operatedArr[0] = modifier.apply(operatedArr[0], receiver));
        String operated = operatedArr[0];
        for (Replacement replacement : replacements) {
            operated = operated.replace(replacement.getBase(), replacement.getReplacement());
        }
        String[] split = operated.split("\n");
        var miniMessageApi = MiniMessage.miniMessage();
        for (String finalMessage : split) {
            AudienceProvider.getBukkitAudiences().sender(receiver).sendMessage(miniMessageApi.deserialize(finalMessage));
        }
    }
    
}
