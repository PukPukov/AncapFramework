package ru.ancap.framework.artifex.implementation.language.input;

import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.CSCommandTarget;
import ru.ancap.framework.command.api.commands.operator.arguments.Accept;
import ru.ancap.framework.command.api.commands.operator.arguments.Argument;
import ru.ancap.framework.command.api.commands.operator.arguments.Arguments;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic.Self;
import ru.ancap.framework.command.api.commands.operator.communicate.Advice;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.Raw;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.StringDelegatePattern;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.language.language.Language;

public class LanguageChangeInput extends CSCommandTarget {

    public LanguageChangeInput() {
        super(new Delegate(
            new Raw(new Advice(new LAPIMessage(Artifex.class, "command.language.enter-language"))),
            new SubCommand(
                new StringDelegatePattern("set"),
                new Arguments(
                    new Accept(new Argument("language", new Self())),
                    dispatch -> {
                        LAPI.setupLanguage(Identifier.of(dispatch.source().sender()), Language.of(dispatch.arguments().get("language", String.class)));
                        Communicator.of(dispatch.source().sender()).message(new LAPIMessage(Artifex.class, "command.language.setup"));
                    }
                )
            )
        ));
    }

}
