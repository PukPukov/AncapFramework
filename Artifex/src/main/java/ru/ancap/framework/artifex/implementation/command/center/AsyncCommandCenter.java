package ru.ancap.framework.artifex.implementation.command.center;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.plugin.api.commands.CommandCenter;
import ru.ancap.framework.plugin.api.exception.CommandNotRegisteredException;

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
        for (String name : plugin.getSettings().commandList()) {
            CommandOperator executor = CommandOperator.EMPTY;
            this.executeRules.put(name, executor);
            this.aliasesMap.put(name, new ArrayList<>());
            for (String additional : plugin.getSettings().aliasesList(name)) {
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
            throw new CommandNotRegisteredException(
                "Command " + commandName + " must be registered in ancapplugin.yml to set its executor!");
        }
        for (String alias : this.aliasesMap.get(commandName)) {
            this.executeRules.put(alias, executor);
        }
        this.executeRules.put(commandName, executor);
    }

    @Override
    public void on(CommandDispatch dispatch) {
        this.operate(
            dispatch.command(),
            commandForm -> commandForm.commandOperator.on(
                new CommandDispatch(
                    dispatch.source(),
                    commandForm.command
                ))
        );
    }

    @Override
    public void on(CommandWrite write) {
        this.operate(
            write.line(),
            commandForm -> commandForm.commandOperator.on(
                new CommandWrite(
                    write.speaker(),
                    commandForm.command
                )
            )
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

    @AllArgsConstructor
    @Data
    private static class CommandForm {
        private final LeveledCommand command;
        private final CommandOperator commandOperator;
    }
}
