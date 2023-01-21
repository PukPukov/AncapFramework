package ru.ancap.framework.api.plugin.plugins.util;

import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.LAPI;
import ru.ancap.framework.api.command.commands.operator.speaking.Adviser;

@AllArgsConstructor
public class LAPIAdviceProvider implements Adviser.AdviseProvider {
    
    private final String id;
    
    @Override
    public String advice(CommandSender sender) {
        return LAPI.localized(id, sender.getName());
    }
}
