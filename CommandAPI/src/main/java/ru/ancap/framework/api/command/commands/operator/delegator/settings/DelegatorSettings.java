package ru.ancap.framework.api.command.commands.operator.delegator.settings;

import ru.ancap.framework.api.command.commands.operator.delegator.subcommand.rule.provide.CommandProvidePattern;

public interface DelegatorSettings {

    CommandProvidePattern getDefaultRule();

}
