package ru.ancap.framework.status;

import org.bukkit.Bukkit;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.communicate.ChatBook;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.*;
import ru.ancap.framework.communicate.message.clickable.ClickableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;
import ru.ancap.framework.status.test.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatusOutput extends CommandTarget {
    
    public StatusOutput(CallableMessage systemName, Iterable<Test> tests) {
        super(new CommandOperator() {
            @Override
            public void on(CommandDispatch dispatch) {
                dispatch.source().communicator().message(new MultilineMessage(
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
                                Set<String> args = new HashSet<>();
                                LeveledCommand parseState = dispatch.command();
                                while (!parseState.isRaw()) {
                                    args.add(parseState.nextArgument());
                                    parseState = parseState.withoutArgument();
                                }
                                
                                Test.TestResult result = test.makeTestFor(
                                    identifier,
                                    new Test.TestingParameters(
                                        args.contains("--skip-hand-tests")
                                    )
                                );
                                CallableMessage represent = switch (result.status()) {
                                    case SUCCESS -> new LAPIMessage(CommonMessageDomains.Status.working);
                                    case SKIPPED -> new BadTestMessage(CommonMessageDomains.Status.testSkipped, result.description());
                                    case FAILURE -> new BadTestMessage(CommonMessageDomains.Status.down, result.description());
                                };
                                return represent.call(identifier);
                            })
                        )
                    )
                ));
            }
            @Override
            public void on(CommandWrite write) {
                write.speaker().sendTab(List.of("--skip-hand-tests"));
            }
        });
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