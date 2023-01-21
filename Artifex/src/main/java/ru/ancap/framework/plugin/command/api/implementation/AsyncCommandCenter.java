package ru.ancap.framework.plugin.command.api.implementation;

import lombok.RequiredArgsConstructor;
import ru.ancap.framework.api.command.commands.command.dispatched.LeveledCommand;
import ru.ancap.framework.api.command.commands.command.event.CommandDispatch;
import ru.ancap.framework.api.command.commands.command.event.CommandWrite;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.framework.api.command.commands.operator.delegator.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.api.plugin.plugins.AncapPlugin;
import ru.ancap.framework.api.plugin.plugins.commands.CommandCenter;
import ru.ancap.framework.api.plugin.plugins.exception.CommandNotRegisteredException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class AsyncCommandCenter implements CommandCenter, CommandOperator, OperateRule {

    private final Map<String, CommandOperator> executeRules = new ConcurrentHashMap<>();

    private final Map<String, List<String>> aliasesMap = new ConcurrentHashMap<>();

    @Override
    public void initialize(AncapPlugin plugin) {
        for (String name : plugin.getSettings().getCommandList()) {
            CommandOperator executor = CommandOperator.EMPTY;
            this.executeRules.put(name, executor);
            this.aliasesMap.put(name, new ArrayList<>());
            for (String additional : plugin.getSettings().getAliasesList(name)) {
                List<String> aliases = this.aliasesMap.get(name);
                aliases.add(additional);
                this.aliasesMap.put(name, aliases);
                this.executeRules.put(additional, executor);
            }
        }
    }

    @Override
    public void setExecutor(String commandName, CommandOperator executor) throws CommandNotRegisteredException {
        CommandOperator previous = this.executeRules.get(commandName);
        if (previous == null) {
            throw new CommandNotRegisteredException("Command "+commandName+" must be registered in ancapplugin.yml to set its executor!");
        }
        for (String alias : this.aliasesMap.get(commandName)) {
            this.executeRules.put(alias, executor);
        }
        this.executeRules.put(commandName, executor);
    }

    @Override
    public void on(CommandDispatch dispatch) {
        this.operate(
                dispatch.dispatched(),
                commandForm -> commandForm.executor.on(
                        new CommandDispatch(
                                dispatch.sender(),
                                commandForm.command
                ))
        );
    }

    @Override
    public void on(CommandWrite write) {
        this.operate(
                write.getWritten(),
                commandForm -> {
                    commandForm.executor.on(
                            new CommandWrite(
                                    write.getSpeaker(),
                                    commandForm.command
                            )
                    );
                }
        );
    }


    private void operate(LeveledCommand command, Consumer<CommandForm> commandFormConsumer) {
        String key = command.nextArgument();
        LeveledCommand finalCommand = command.withoutArgument();
        new Thread(() -> {
            CommandOperator rule = this.executeRules.get(key);
            commandFormConsumer.accept(
                    new CommandForm(
                            finalCommand,
                            rule
                    )
            );
        }).start();

    }


    @Override
    public boolean isOperate(LeveledCommand command) {
        if (command.isRaw()) return false;
        return this.executeRules.containsKey(command.nextArgument());
    }

    private record CommandForm(LeveledCommand command, CommandOperator executor) {}
}
