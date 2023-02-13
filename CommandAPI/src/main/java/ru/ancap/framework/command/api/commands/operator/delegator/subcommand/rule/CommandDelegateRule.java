package ru.ancap.framework.command.api.commands.operator.delegator.subcommand.rule;

import ru.ancap.framework.command.api.commands.operator.delegator.subcommand.rule.delegate.DelegatePattern;
import ru.ancap.framework.command.api.commands.operator.delegator.subcommand.rule.provide.CommandProvidePattern;

public interface CommandDelegateRule extends DelegatePattern, CommandProvidePattern {

}
