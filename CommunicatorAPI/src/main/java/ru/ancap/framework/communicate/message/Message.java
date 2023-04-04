package ru.ancap.framework.communicate.message;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.ChatColor;
import ru.ancap.framework.communicate.MiniMessageMapper;
import ru.ancap.framework.communicate.replacement.Replacement;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class Message extends CacheMessage implements CallableMessage {

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
