package ru.ancap.framework.artifex;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.operator.communicate.ChatBook;
import ru.ancap.framework.command.api.commands.operator.communicate.Reply;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.Raw;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.StringDelegatePattern;
import ru.ancap.framework.command.api.commands.operator.exclusive.Exclusive;
import ru.ancap.framework.command.api.commands.operator.exclusive.OP;
import ru.ancap.framework.command.api.commands.operator.exclusive.Permission;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.plugin.api.Ancap;
import ru.ancap.framework.plugin.api.information.AuthorsSupplier;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleHandle;
import ru.ancap.framework.plugin.api.language.locale.loader.LocaleReloadInput;
import ru.ancap.framework.plugin.util.InDevMessage;
import ru.ancap.framework.status.StatusOutput;
import ru.ancap.framework.status.test.Test;

import java.util.List;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class ArtifexCommandExecutor extends CommandTarget {
    
    public ArtifexCommandExecutor(Ancap ancap, List<Test> tests, LocaleHandle localeHandle) {
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
                            dispatch -> dispatch.source().communicator().message(InDevMessage.instance())
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
            ),
            new SubCommand(
                new StringDelegatePattern("plugins"),
                new Exclusive(
                    new Permission("artifex.view-artifex-plugins"),
                    new Reply(() -> new LAPIMessage(
                        Artifex.class, "command.plugins.base-message",
                        new Placeholder("plugins", new ChatBook<>(Artifex.PLUGIN().ancapPlugins(), plugin -> new LAPIMessage(
                            Artifex.class, "command.plugins.plugin-form",
                            new Placeholder("name", plugin.getName()),
                            new Placeholder("version", plugin.getDescription().getVersion()),
                            new Placeholder("authors", plugin.getDescription().getAuthors())
                        )))
                    ))
                )
            ),
            new SubCommand(
                new StringDelegatePattern("reload"),
                new Delegate(
                    new SubCommand(
                        new StringDelegatePattern("locales"),
                        new LocaleReloadInput(localeHandle)
                    )
                )
            )
        ));
    }
    
}