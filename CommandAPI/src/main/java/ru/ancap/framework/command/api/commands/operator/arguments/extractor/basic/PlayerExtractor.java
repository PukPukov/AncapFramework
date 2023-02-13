package ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerExtractor extends PrimitiveExtractor<Player> {

    public PlayerExtractor() {
        super(Player.class);
    }

    @Override
    protected Player provide(String string) throws Throwable {
        return Bukkit.getPlayer(string);
    }
    
}
