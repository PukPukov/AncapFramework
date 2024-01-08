package ru.ancap.framework.command.api.commands.operator.delegate;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.syntax.CSCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.settings.ClassicDelegatorSettings;
import ru.ancap.framework.command.api.commands.operator.delegate.settings.DelegatorSettings;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.CommandDelegateRule;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.provide.CommandProvidePattern;

import java.util.ArrayList;
import java.util.List;

@ToString @EqualsAndHashCode
public class Delegate implements CSCommandOperator {

    private final CommandProvidePattern defaultRule;
    private final List<CommandDelegateRule> rules;
    private final List<String> totalCandidates;

    private Delegate(CommandProvidePattern defaultRule, List<CommandDelegateRule> provideRules) {
        this.defaultRule = defaultRule;
        this.rules = provideRules;
        List<String> totalComplete = new ArrayList<>();
        provideRules.forEach(rule -> totalComplete.addAll(rule.candidates()));
        this.totalCandidates = totalComplete;
    }

    public Delegate(DelegatorSettings settings, CommandDelegateRule... provideRules) {
        this(settings.getDefaultRule(), List.of(provideRules));
    }

    public Delegate(CommandDelegateRule... provideRules) {
        this(new ClassicDelegatorSettings(), provideRules);
    }

    @Override
    public void on(CommandDispatch dispatch) {
        CommandProvidePattern pattern = this.ruleFor(dispatch.command());
        pattern.delegated().on(
            new CommandDispatch(
                dispatch.source(),
                pattern.convert(dispatch.command())
            )
        );
    }

    @Override
    public void on(CommandWrite write) {
        CSCommand command = write.line();
        if (command.isRaw()) {
            write.speaker().sendTab(this.totalCandidates);
            return;
        }
        CommandProvidePattern pattern = this.ruleFor(command);
        pattern.delegated().on(new CommandWrite(write.speaker(), pattern.convert(command)));
    }

    private CommandProvidePattern ruleFor(CSCommand command) {
        for (CommandDelegateRule rule : this.rules) if (rule.isOperate(command)) return rule;
        return this.defaultRule;
    }

}
