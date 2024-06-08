package ru.ancap.framework.communicate.message;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.ChatColor;
import ru.ancap.framework.communicate.MiniMessageMapper;
import ru.ancap.framework.communicate.modifier.Modifier;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class Message extends WrapperMessage implements CallableMessage {

    public static final CallableMessage EMPTY = new Message("");
    
    public Message(Object base, Modifier... modifiers) {
        this(String.valueOf(base), modifiers);
    }

    public Message(final String base, final Modifier... modifiers) {
        super((caller) -> {
            String operated = base;
            
            // Base modifiers—legacy color codes (with "&" symbol) and \n support
            {
                operated = ChatColor.translateAlternateColorCodes('&', operated);
                operated = MiniMessageMapper.mapLegacy(operated);
                operated = operated.replace("\n", "<newline>");
                operated = operated.replaceAll("<newline>", "<newline><reset>");
            }
            
            // PlaceholderAPI modifiers. Currently not supported because I haven't created the architecture yet
            {
                
            }
            
            // custom modifiers
            for (Modifier modifier : modifiers) operated = modifier.apply(operated, caller);
            return operated;
        });
    }
    
}