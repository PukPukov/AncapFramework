package ru.ancap.framework.command.api.commands.operator.delegator.settings;

import ru.ancap.framework.command.api.commands.operator.delegator.subcommand.rule.provide.CommandProvidePattern;

public interface DelegatorSettings {

    CommandProvidePattern getDefaultRule();

}
