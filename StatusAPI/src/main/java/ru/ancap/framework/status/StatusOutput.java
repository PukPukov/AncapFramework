package ru.ancap.framework.status;

import org.bukkit.Bukkit;
import ru.ancap.commons.Pair;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.operator.communicate.Reply;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.*;
import ru.ancap.framework.communicate.message.clickable.ClickableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;
import ru.ancap.framework.status.test.Test;

import java.util.Collection;

public class StatusOutput extends CommandTarget {

    public StatusOutput(CallableMessage systemName, Collection<Test> tests) {
        super(new Reply((source) -> {
            // completed tests list preparation
            Iterable<Pair<Test, Test.TestResult>> completedTests = tests.stream()
                .map(test -> new Pair<>(test, test.makeTestFor(Identifier.of(source.sender()) /* <- test runs */)))
                .toList();

            // message building
            return new MultilineMessage(
                new LAPIMessage(
                    CommonMessageDomains.Status.top,
                    new Placeholder("system name", systemName)
                ),
                new ChatBook<>(
                    completedTests,
                    test -> new LAPIMessage(
                        CommonMessageDomains.Status.testForm,
                        new Placeholder("module name", test.key().name()),
                        new Placeholder("status", switch (test.value().status()) {
                            case SUCCESS -> new LAPIMessage(CommonMessageDomains.Status.working);
                            case SKIPPED -> new BadTestMessage(CommonMessageDomains.Status.testSkipped, test.value().description());
                            case FAILURE -> new BadTestMessage(CommonMessageDomains.Status.down, test.value().description());
                        })
                    )
                )
            );
        }));
    }

    private static class BadTestMessage extends WrapperMessage {
        
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
