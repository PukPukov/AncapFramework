package ru.ancap.framework.artifex.implementation.language.input;

import org.bukkit.Bukkit;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.artifex.implementation.language.flow.LanguageChangeEvent;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.operator.arguments.Argument;
import ru.ancap.framework.command.api.commands.operator.arguments.Arguments;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic.Self;
import ru.ancap.framework.command.api.commands.operator.communicate.Adviser;
import ru.ancap.framework.command.api.commands.operator.delegator.CommandDelegator;
import ru.ancap.framework.command.api.commands.operator.delegator.subcommand.Raw;
import ru.ancap.framework.command.api.commands.operator.delegator.subcommand.SubCommand;
import ru.ancap.framework.command.api.commands.operator.delegator.subcommand.rule.delegate.StringDelegatePattern;
import ru.ancap.framework.language.additional.LAPIMessage;

import java.util.List;

public class LanguageCommandExecutor extends CommandTarget {

    public LanguageCommandExecutor() {
        super(
                new CommandDelegator(
                        new Raw(
                                new Adviser(new LAPIMessage(Artifex.class, "command.language.enter-language"))
                        ),
                        new SubCommand(
                                new StringDelegatePattern("set"),
                                new Arguments(
                                        List.of(
                                                new Argument("language", new Self())
                                        ),
                                        dispatch -> Bukkit.getPluginManager().callEvent(
                                                new LanguageChangeEvent(
                                                        dispatch.source().sender(), 
                                                        dispatch.arguments().get("language", String.class)
                                                )
                                        )
                                )
                        )
                )
        );
    }

}
