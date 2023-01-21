package ru.ancap.framework.api.command.commands.operator.finite.pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;

public class ArgumentCutter implements CommandEventPattern {

    private final SenderEventProvider provider;

    public ArgumentCutter(SenderEventProvider provider) {
        this.provider = provider;
    }

    @Override
    public Event patternalize(CommandSender sender, LeveledCommand command) {
        return this.provider.event(sender);
    }

    @FunctionalInterface
    public interface SenderEventProvider {
        Event event(CommandSender sender);
    }

}
