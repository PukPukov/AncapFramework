package ru.ancap.framework.artifex;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.utility.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.operator.communicate.Reply;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.Raw;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.StringDelegatePattern;
import ru.ancap.framework.command.api.commands.operator.exclusive.Exclusive;
import ru.ancap.framework.command.api.commands.operator.exclusive.OP;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.plugin.api.Ancap;
import ru.ancap.framework.plugin.api.AncapFrameworkAPI;
import ru.ancap.framework.plugin.api.information.AuthorsSupplier;
import ru.ancap.framework.runtime.Artifex;
import ru.ancap.framework.status.StatusOutput;
import ru.ancap.framework.status.test.Test;

import java.util.List;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class ArtifexCSCommandExecutor extends CommandTarget {
    
    public ArtifexCSCommandExecutor(AncapFrameworkAPI ancap, List<Test> tests) {
        super(new Delegate(
            new Raw(new AuthorsSupplier(Artifex.PLUGIN())),
            new SubCommand(
                new StringDelegatePattern("tps"),
                new Reply(() -> new LAPIMessage(
                    Artifex.class, "command.tps",
                    new Placeholder("tps", ancap.getServerTPS())
                ))
            ),
            new SubCommand(
                new StringDelegatePattern("benchmark"),
                new Exclusive(
                    new OP(),
                    new Delegate(
                        new SubCommand(
                            new StringDelegatePattern("command-api"),
                            dispatch -> {
                                // TODO 
                                // TODO InDevMessage to use it here like dispatch.communicator().message(InDevMessage.instance())
                            }
                        ),
                        new SubCommand(
                            new StringDelegatePattern("dummy"),
                            dispatch -> {}
                        )
                    )
                )
            ),
            new SubCommand(
                new StringDelegatePattern("status"),
                new Exclusive(
                    new OP(),
                    new StatusOutput(new Message("<color:#e51e1e>AncapFramework</color:#e51e1e>"), tests)
                )
            )
        ));
    }
    
}