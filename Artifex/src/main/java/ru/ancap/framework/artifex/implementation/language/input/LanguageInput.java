package ru.ancap.framework.artifex.implementation.language.input;

import org.bukkit.Bukkit;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.CommandTarget;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.arguments.Accept;
import ru.ancap.framework.command.api.commands.operator.arguments.Argument;
import ru.ancap.framework.command.api.commands.operator.arguments.Arguments;
import ru.ancap.framework.command.api.commands.operator.arguments.extractor.basic.Self;
import ru.ancap.framework.command.api.commands.operator.communicate.Advice;
import ru.ancap.framework.command.api.commands.operator.communicate.ChatBook;
import ru.ancap.framework.command.api.commands.operator.communicate.Reply;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.Raw;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.command.api.commands.operator.exclusive.Exclusive;
import ru.ancap.framework.command.api.commands.operator.exclusive.OP;
import ru.ancap.framework.command.api.event.classic.NotEnoughArgumentsEvent;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.ColoredMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.message.MultilineMessage;
import ru.ancap.framework.communicate.message.clickable.ClickableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.identifier.Identifier;
import ru.ancap.framework.language.LAPI;
import ru.ancap.framework.language.additional.LAPIDomain;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.language.language.Language;
import ru.ancap.framework.plugin.api.AncapBukkit;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class LanguageInput extends CommandTarget {
    
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.#");

    public LanguageInput(Language default_) {
        super(new Delegate(
            new Raw(new Advice(new LAPIMessage(Artifex.class, "command.language.enter-language"))),
            new SubCommand("set", new Arguments(
                new Accept(new Argument("language", new Self())),
                dispatch -> {
                    LAPI.updateLanguage(Identifier.of(dispatch.source().sender()), Language.of(dispatch.arguments().get("language", String.class)));
                    Communicator.of(dispatch.source().sender()).message(new LAPIMessage(Artifex.class, "command.language.setup"));
                })
            ),
            new SubCommand("list", new Reply(() -> new MultilineMessage(
                new LAPIMessage(Artifex.class, "command.language.list.header"),
                new ChatBook<>(
                    LAPI.allLanguages().stream()
                        .sorted((Comparator.comparingInt(language -> LAPI.statistic(language).localisedLines())))
                        .toList(),
                    language -> new LAPIMessage(
                        Artifex.class, "command.language.list.entry", 
                        new Placeholder("self name", new Message(LAPI.localized(LAPIDomain.of(Artifex.class, "command.language.list.self-name"), language))), 
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
                            return new ColoredMessage(
                                new Message(decimalFormat.format(percentage)), 
                                new Message(color)
                            ).call(identifier);
                        }), 
                        new Placeholder("select button", new ClickableMessage(
                            new Message(LAPI.localized(LAPIDomain.of(Artifex.class, "command.language.list.select"), language)), 
                            click -> AncapBukkit.sendCommand(click.clicker(), "language set "+language.code())
                        ))
                ))
            ))),
            new SubCommand("compare", new Exclusive(
                new OP(),
                new CommandOperator() {
                    @Override
                    public void on(CommandWrite write) {
                        if (write.line().arguments().size() <= 1) write.speaker().sendTab(LAPI.allLanguages().stream().map(Language::code).toList());
                    }
                    
                    @Override
                    public void on(CommandDispatch dispatch) {
                        LeveledCommand parseState = dispatch.command();
                        if (parseState.isRaw()) {
                            Bukkit.getPluginManager().callEvent(new NotEnoughArgumentsEvent(dispatch.source().sender(), 2));
                            return;
                        }
                        String languageOne = parseState.nextArgument();
                        parseState = parseState.withoutArgument();
                        if (parseState.isRaw()) {
                            Bukkit.getPluginManager().callEvent(new NotEnoughArgumentsEvent(dispatch.source().sender(), 1));
                            return;
                        }
                        String languageTwo = parseState.nextArgument();
//                      parseState = parseState.withoutArgument();
                        
                        Set<String> keysOne = LAPI.allKeys(Language.of(languageOne));
                        Set<String> keysTwo = LAPI.allKeys(Language.of(languageTwo));
                        Set<String> onlyInOne = keysOne.stream()
                            .filter(key -> !keysTwo.contains(key))
                            .collect(Collectors.toSet());
                        Set<String> onlyInTwo = keysTwo.stream()
                            .filter(key -> !keysOne.contains(key))
                            .collect(Collectors.toSet());
                        dispatch.source().communicator().message(new MultilineMessage(
                            new LAPIMessage(
                                Artifex.class, "command.language.compare.header",
                                new Placeholder("code", languageOne)
                            ),
                            new ChatBook<>(onlyInOne, Message::new),
                            new LAPIMessage(
                                Artifex.class, "command.language.compare.header",
                                new Placeholder("code", languageTwo)
                            ),
                            new ChatBook<>(onlyInTwo, Message::new)
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
                        LeveledCommand parseState = dispatch.command();
                        if (parseState.isRaw()) {
                            Bukkit.getPluginManager().callEvent(new NotEnoughArgumentsEvent(dispatch.source().sender(), 2));
                            return;
                        }
                        String language = parseState.nextArgument();
//                      parseState = parseState.withoutArgument();
                        
                        Set<String> keys = LAPI.allKeys(Language.of(language));
                        dispatch.source().communicator().message(new MultilineMessage(
                            new LAPIMessage(
                                Artifex.class, "command.language.view.header",
                                new Placeholder("code", language)
                            ),
                            new ChatBook<>(keys, Message::new)
                        ));
                    }
                }
            ))
        ));
    }

}