package ru.ancap.framework.artifex;

import ru.ancap.framework.communicate.replacement.Placeholder;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.artifex.implementation.plugin.AuthorsSupplier;
import ru.ancap.framework.command.api.commands.operator.communicate.Reply;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.Raw;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.StringDelegatePattern;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.plugin.api.Ancap;

public class ArtifexCommandExecutor extends CommandTarget {
    public ArtifexCommandExecutor(Ancap ancap) {
        super(new Delegate(
                new Raw(new AuthorsSupplier(Artifex.PLUGIN)),
                new SubCommand(
                        new StringDelegatePattern("tps"),
                        new Reply(() -> new LAPIMessage(
                                Artifex.class, "command.tps",
                                new Placeholder("tps", ancap.getServerTPS())
                        ))
                )
        ));
    }
}
