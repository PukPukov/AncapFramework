package ru.ancap.framework.artifex.implementation.command.center;

import lombok.RequiredArgsConstructor;
import ru.ancap.framework.artifex.implementation.command.object.SenderSource;
import ru.ancap.framework.command.api.commands.object.conversation.CommandLineSpeaker;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;
import ru.ancap.framework.command.api.commands.object.tab.TabBundle;
import ru.ancap.framework.command.api.syntax.CSCommandParser;
import ru.ancap.framework.command.api.syntax.SimpleCSCommandParser;
import ru.ancap.framework.plugin.api.commands.BukkitCommandDispatch;
import ru.ancap.framework.plugin.api.commands.BukkitCommandWrite;
import ru.ancap.framework.plugin.api.commands.CommandEventHandler;

@RequiredArgsConstructor
public class CSAPIEventHandler implements CommandEventHandler {
    
    private final CSCommandParser parser = new SimpleCSCommandParser();
    private final CSCommandOperator operator;

    @Override
    public void on(BukkitCommandDispatch dispatch) {
        this.operator.on(new CommandDispatch(
            dispatch::sender,
            this.parser.parse(dispatch.line())
        ));
    }

    @Override
    public void on(BukkitCommandWrite write) {
        this.operator.on(new CommandWrite(
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
