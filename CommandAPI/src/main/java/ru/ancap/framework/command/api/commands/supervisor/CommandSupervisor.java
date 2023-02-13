package ru.ancap.framework.command.api.commands.supervisor;

import ru.ancap.framework.command.api.commands.operator.delegator.CommandDelegator;
import ru.ancap.framework.command.api.commands.operator.delegator.settings.DelegatorSettings;
import ru.ancap.framework.command.api.commands.operator.delegator.subcommand.rule.CommandDelegateRule;

public class CommandSupervisor extends CommandDelegator {

    public CommandSupervisor(DelegatorSettings settings, CommandDelegateRule... provideRules) {
        super(settings, provideRules);
    }

    public CommandSupervisor(CommandDelegateRule... provideRules) {
        super(provideRules);
    }

    // Задачи супервайзора
    // 1. Создавать GUI (/<command> gui)
    // 2. Создавать /<command> help
    // 3. Обрабатывать сырые команды и по выбору отдавать либо gui, либо help, либо не обрабатывать
}
