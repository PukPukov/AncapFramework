package ru.ancap.framework.plugin;

import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.LAPI;
import ru.ancap.framework.api.command.util.TypeNameProvider;

public class LAPITypeNameProvider implements TypeNameProvider {
    
    @Override
    public String apply(Class<?> type, CommandSender sender) {
        return LAPI.localized("ru.ancap.types."+type.getName(), sender.getName());
    }
}
