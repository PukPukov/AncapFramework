package ru.ancap.framework.communicate.message;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.ChatColor;
import ru.ancap.framework.communicate.MiniMessageMapper;
import ru.ancap.framework.communicate.modifier.Modifier;

@ToString(callSuper = true) @EqualsAndHashCode
@RequiredArgsConstructor
public class Text implements CallableText {

    public static final CallableText EMPTY = new Text("");
    
    private final String base;
    private final Modifier[] modifiers;
    
    public Text(Object base, Modifier... modifiers) {
        this(String.valueOf(base), modifiers);
    }
    
    @Override
    public String call(String identifier) {
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
        for (Modifier modifier : modifiers) operated = modifier.apply(operated, identifier);
        return operated;
    }
}