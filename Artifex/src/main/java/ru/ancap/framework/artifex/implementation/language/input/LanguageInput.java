package ru.ancap.framework.artifex.implementation.language.input;

import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.object.dispatched.LCParseState;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.communicate.Advice;
import ru.ancap.framework.command.api.commands.operator.communicate.ChatBook;
import ru.ancap.framework.command.api.commands.operator.communicate.Reply;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.Raw;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.command.api.commands.operator.exclusive.Exclusive;
import ru.ancap.framework.command.api.commands.operator.exclusive.OP;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.ColoredText;
import ru.ancap.framework.communicate.message.Text;
import ru.ancap.framework.communicate.message.MultilineText;
import ru.ancap.framework.communicate.message.clickable.ClickableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.additional.LAPIDomain;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.plugin.api.AncapBukkit;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.ancap.framework.command.api.commands.exception.CommandExceptions.noArgument;

public class LanguageInput extends CommandTarget {
    
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    public LanguageInput(Language default_) {
        super(new Delegate(
            new Raw(new Advice(new LAPIText(Artifex.class, "command.language.enter-language"))),
            new SubCommand("set", dispatch -> {
                LCParseState onLanguage = dispatch.command().step(noArgument(() -> new LAPIText(Artifex.class, "arguments.new-language")));
                LAPI.updateLanguage(Identifier.of(dispatch.source().sender()), Language.of(onLanguage.part().main()));
                Communicator.of(dispatch.source().sender()).message(new LAPIText(Artifex.class, "command.language.setup"));
            }),
            new SubCommand("list", new Reply(() -> new MultilineText(
                new LAPIText(Artifex.class, "command.language.list.header"),
                new ChatBook<>(
                    LAPI.allLanguages().stream()
                        .sorted((Comparator.<Language>comparingInt(language -> LAPI.statistic(language).localisedLines()).reversed()))
                        .toList(),
                    language -> new LAPIText(
                        Artifex.class, "command.language.list.entry", 
                        new Placeholder("self name", new Text(LAPI.localized(LAPIDomain.of(Artifex.class, "command.language.list.self-name"), language))), 
                        new Placeholder("code", language.code()), 
                        new Placeholder("percentage", identifier -> {
                            double percentage = ((double) LAPI.statistic(language).localisedLines() / (double) LAPI.statistic(default_).localisedLines()) * 100;
                            String color;
                            if      (percentage < 20)  color = "#ed0000";
                            else if (percentage < 40)  color = "#b34a00";
                            else if (percentage < 60)  color = "#b3ad00";
                            else if (percentage < 80)  color = "#83b300";
                            else if (percentage < 100) color = "#44b300";
                            else                       color = "#12d600";
                            return new ColoredText(
                                new Text(decimalFormat.format(percentage)), 
                                new Text(color)
                            ).call(identifier);
                        }), 
                        new Placeholder("select button", new ClickableText(
                            new Text(LAPI.localized(LAPIDomain.of(Artifex.class, "command.language.list.select"), language)), 
                            click -> AncapBukkit.sendCommand(click.clicker(), "language set "+language.code())
                        ))
                ))
            ))),
            new SubCommand("compare", new Exclusive(
                new OP(),
                new CommandOperator() {
                    @Override
                    public void on(CommandWrite write) {
                        if (write.line().parts().size() <= 1) write.speaker().sendTab(LAPI.allLanguages().stream().map(Language::code).toList());
                    }
                    
                    @Override
                    public void on(CommandDispatch dispatch) {
                        LCParseState onLanguageOne = dispatch.command().step(noArgument(() -> new LAPIText(Artifex.class, "arguments.compared-language")));
                        LCParseState onLanguageTwo = onLanguageOne.command().step(noArgument(() -> new LAPIText(Artifex.class, "arguments.compared-language")));
                        
                        Set<String> keysOne = LAPI.allKeys(Language.of(onLanguageOne.part().main()));
                        Set<String> keysTwo = LAPI.allKeys(Language.of(onLanguageTwo.part().main()));
                        Set<String> onlyInOne = keysOne.stream()
                            .filter(key -> !keysTwo.contains(key))
                            .collect(Collectors.toSet());
                        Set<String> onlyInTwo = keysTwo.stream()
                            .filter(key -> !keysOne.contains(key))
                            .collect(Collectors.toSet());
                        dispatch.source().communicator().message(new MultilineText(
                            new LAPIText(
                                Artifex.class, "command.language.compare.header",
                                new Placeholder("code", onLanguageOne.part().main())
                            ),
                            new ChatBook<>(onlyInOne, Text::new),
                            new LAPIText(
                                Artifex.class, "command.language.compare.header",
                                new Placeholder("code", onLanguageTwo.part().main())
                            ),
                            new ChatBook<>(onlyInTwo, Text::new)
                        ));
                    }
                }
            )),
            new SubCommand("view", new Exclusive(
                new OP(),
                new CommandOperator() {
                    @Override
                    public void on(CommandWrite write) {
                        if (write.line().isRaw()) write.speaker().sendTab(LAPI.allLanguages().stream().map(Language::code).toList());
                    }
                    
                    @Override
                    public void on(CommandDispatch dispatch) {
                        LCParseState onLanguage = dispatch.command().step(noArgument(() -> new LAPIText(Artifex.class, "arguments.viewed-language")));
                        
                        Set<String> keys = LAPI.allKeys(Language.of(onLanguage.part().main()));
                        dispatch.source().communicator().message(new MultilineText(
                            new LAPIText(
                                Artifex.class, "command.language.view.header",
                                new Placeholder("code", onLanguage.part().main())
                            ),
                            new ChatBook<>(keys, Text::new)
                        ));
                    }
                }
            ))
        ));
    }

}