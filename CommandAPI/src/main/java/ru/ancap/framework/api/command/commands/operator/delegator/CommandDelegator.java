package ru.ancap.framework.api.command.commands.operator.delegator;

import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.event.CommandDispatch;
import ru.ancap.framework.api.command.commands.command.event.CommandWrite;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.framework.api.command.commands.operator.delegator.settings.ClassicDelegatorSettings;
import ru.ancap.framework.api.command.commands.operator.delegator.settings.DelegatorSettings;
import ru.ancap.framework.api.command.commands.operator.delegator.subcommand.rule.CommandDelegateRule;
import ru.ancap.framework.api.command.commands.operator.delegator.subcommand.rule.provide.CommandProvidePattern;

import java.util.List;

/**
 * `CommandDelegator` is a `CommandExecutor` that delegates commands to other `CommandExecutor`s based on a set of rules
 */
public class CommandDelegator implements CommandOperator {

    private final CommandProvidePattern defaultRule;
    private final List<CommandDelegateRule> rules;

    private CommandDelegator(CommandProvidePattern defaultRule, List<CommandDelegateRule> provideRules) {
        this.defaultRule = defaultRule;
        this.rules = provideRules;
    }

    public CommandDelegator(DelegatorSettings settings, CommandDelegateRule... provideRules) {
        this(
                settings.getDefaultRule(),
                List.of(provideRules)
        );
    }

    public CommandDelegator(CommandDelegateRule... provideRules) {
        this(
                new ClassicDelegatorSettings(),
                provideRules
        );
    }

    @Override
    public void on(CommandDispatch dispatch) {
        CommandProvidePattern pattern = this.ruleFor(dispatch.dispatched());
        pattern.delegated().on(
                new CommandDispatch(
                        pattern.convert(dispatch.sender()),
                        pattern.convert(dispatch.dispatched())
                )
        );
    }

    @Override
    public void on(CommandWrite write) {
        CommandProvidePattern pattern = this.ruleFor(write.getWritten());
        pattern.delegated().on(
                new CommandWrite(
                        write.getSpeaker(),
                        write.getWritten()
                )
        );
    }

    private CommandProvidePattern ruleFor(LeveledCommand command) {
        for (CommandDelegateRule rule : this.rules) {
            if (rule.isOperate(command)) {
                return rule;
            }
        }
        return defaultRule;
    }

}
