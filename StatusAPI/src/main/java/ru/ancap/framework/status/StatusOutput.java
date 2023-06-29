package ru.ancap.framework.status;

import org.bukkit.Bukkit;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.operator.communicate.ChatBook;
import ru.ancap.framework.command.api.commands.operator.communicate.Reply;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.*;
import ru.ancap.framework.communicate.message.clickable.ClickableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;
import ru.ancap.framework.status.test.Test;

public class StatusOutput extends CommandTarget {

    public StatusOutput(CallableMessage systemName, Iterable<Test> tests) {
        super(new Reply(() -> new MultilineMessage(
            new LAPIMessage(
                CommonMessageDomains.Status.top,
                new Placeholder("system name", systemName)
            ),
            new ChatBook<>(
                tests,
                test -> new LAPIMessage(
                    CommonMessageDomains.Status.testForm,
                    new Placeholder("module name", test.name()),
                    new Placeholder("status", identifier -> {
                        Test.TestResult result = test.makeTestFor(identifier);
                        CallableMessage represent = switch (result.status()) {
                            case SUCCESS -> new LAPIMessage(CommonMessageDomains.Status.working);
                            case SKIPPED -> new BadTestMessage(CommonMessageDomains.Status.testSkipped, result.description());
                            case FAILURE -> new BadTestMessage(CommonMessageDomains.Status.down, result.description());
                        };
                        return represent.call(identifier);
                    })
                )
            )
        )));
    }

    private static class BadTestMessage extends CacheMessage {
        
        public BadTestMessage(String mainMessageDomain, CallableMessage description) {
            super(new ClickableMessage(
                new HoveredMessage(
                    new LAPIMessage(mainMessageDomain),
                    new LAPIMessage(CommonMessageDomains.Status.pressToPrintDescription)
                ),
                click -> {
                    Communicator.of(click.clicker()).message(new LAPIMessage(CommonMessageDomains.sentToConsole));
                    Communicator.of(Bukkit.getConsoleSender()).message(new MultilineMessage(Message.EMPTY, description));
                }
            ));
        }
        
    }
    
}
