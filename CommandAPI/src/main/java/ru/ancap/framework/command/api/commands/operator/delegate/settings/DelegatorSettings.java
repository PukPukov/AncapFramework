package ru.ancap.framework.command.api.commands.operator.delegate.settings;

import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.provide.CommandProvidePattern;

public interface DelegatorSettings {

    CommandProvidePattern getDefaultRule();

}
