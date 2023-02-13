package ru.ancap.communicate.message;

import org.bukkit.ChatColor;
import ru.ancap.communicate.MiniMessageMapper;
import ru.ancap.communicate.replacement.Replacement;

public class Message extends WrapperMessage implements CallableMessage {

    public Message(final String base, final Replacement... replacements) {
        super((caller) -> {
            String operated = base;
            operated = ChatColor.translateAlternateColorCodes('&', operated);
            operated = MiniMessageMapper.mapLegacy(operated);
            operated = operated.replace("\n", "<newline>");
            for (Replacement replacement : replacements) {
                operated = operated.replace(replacement.base(), replacement.call(caller));
            }
            return operated;
        });
    }
    
}
