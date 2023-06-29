package ru.ancap.framework.command.api.commands.supervisor;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.settings.DelegatorSettings;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.CommandDelegateRule;

@ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
public class Supervise extends Delegate {

    public Supervise(DelegatorSettings settings, CommandDelegateRule... provideRules) {
        super(settings, provideRules);
    }

    public Supervise(CommandDelegateRule... provideRules) {
        super(provideRules);
    }

    // Задачи супервайзора
    // 1. Создавать GUI (/<command> gui)
    // 2. Создавать /<command> help
    // 3. Обрабатывать сырые команды и по выбору отдавать либо gui, либо help, либо не обрабатывать
    
}
