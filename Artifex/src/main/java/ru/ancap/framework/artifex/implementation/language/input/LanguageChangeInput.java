package ru.ancap.framework.artifex.implementation.language.input;

import org.bukkit.Bukkit;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.artifex.implementation.language.flow.LanguageChangeEvent;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.operator.arguments.Accept;
import ru.ancap.framework.command.api.commands.operator.arguments.Argument;
import ru.ancap.framework.command.api.commands.operator.arguments.Arguments;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic.Self;
import ru.ancap.framework.command.api.commands.operator.communicate.Advice;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.Raw;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.StringDelegatePattern;
import ru.ancap.framework.language.additional.LAPIMessage;

public class LanguageChangeInput extends CommandTarget {

    public LanguageChangeInput() {
        super(new Delegate(
                new Raw(
                        new Advice(new LAPIMessage(Artifex.class, "command.language.enter-language"))
                ),
                new SubCommand(
                        new StringDelegatePattern("set"),
                        new Arguments(
                                new Accept(
                                        new Argument("language", new Self())
                                ),
                                dispatch -> Bukkit.getPluginManager().callEvent(new LanguageChangeEvent(
                                        dispatch.source().sender(), 
                                        dispatch.arguments().get("language", String.class)
                                ))
                        )
                )
        ));
    }

}
