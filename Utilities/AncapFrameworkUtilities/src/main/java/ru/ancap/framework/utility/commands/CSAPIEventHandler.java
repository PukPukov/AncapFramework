package ru.ancap.framework.utility.commands;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.ancap.framework.api.center.command.BukkitCommandDispatch;
import ru.ancap.framework.api.center.command.BukkitCommandWrite;
import ru.ancap.framework.api.center.command.CommandEventHandler;
import ru.ancap.framework.artifex.implementation.command.object.SenderSource;
import ru.ancap.framework.command.api.commands.object.conversation.CommandLineSpeaker;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;
import ru.ancap.framework.command.api.syntax.CSCommandParser;
import ru.ancap.framework.command.api.syntax.SimpleCSCommandParser;
import ru.ancap.framework.mccsyntax.bukkitadv.tab.TabBundle;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class CSAPIEventHandler implements CommandEventHandler {
    
    private final CSCommandParser parser = new SimpleCSCommandParser();
    private final CSCommandOperator wrapped;
    
    @Override
    public void on(BukkitCommandDispatch dispatch) {
        this.wrapped.on(new CommandDispatch(
            dispatch::sender,
            this.parser.parse(dispatch.line())
        ));
    }
    
    @Override
    public void on(BukkitCommandWrite write) {
        this.wrapped.on(new CommandWrite(
            new CommandLineSpeaker() {
                @Override
                public CommandSource source() {
                    return new SenderSource(write.writer().sender());
                }
                
                @Override
                public void sendTab(TabBundle tab) {
                    
                }
            }
        ));
    }
    
}