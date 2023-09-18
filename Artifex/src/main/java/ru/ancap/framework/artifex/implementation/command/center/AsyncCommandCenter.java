package ru.ancap.framework.artifex.implementation.command.center;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.command.CommandExecutor;
import org.jetbrains.annotations.Nullable;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.plugin.api.AncapBukkit;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.plugin.api.commands.CommandCenter;
import ru.ancap.framework.plugin.api.commands.CommandHandleState;
import ru.ancap.framework.plugin.api.exception.CommandAlreadyRegisteredException;
import ru.ancap.framework.plugin.api.exception.CommandNotRegisteredException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class AsyncCommandCenter implements CommandCenter, CommandOperator, OperateRule {
    
    /* id is primary command name */
    private final Map<String /*command*/, String /*id*/      > redirectMap  = new ConcurrentHashMap<>();
    private final Map<String /*id*/,      CommandHandleState > handleStates = new ConcurrentHashMap<>();
    
    private final CommandExecutor proxy;

    @Override
    public void initialize(AncapPlugin plugin) {
        for (String commandName : plugin.getSettings().getCommandList()) {
            this.register(commandName, new CommandHandleState(
                commandName, 
                plugin.getSettings().getAliasesList(commandName), 
                plugin, 
                CommandOperator.EMPTY
            ));
        }
    }
    
    @Override
    public void register(String commandName, CommandHandleState state) {
        CommandHandleState previous = this.handleStates.get(commandName);
        if (previous != null) throw new CommandAlreadyRegisteredException(commandName);
        
        List<String> totalExecutes = this.totalExecutesFor(state);
        for (String redirect : totalExecutes) this.redirectMap.put(redirect, commandName);
        this.handleStates.put(commandName, new CommandHandleState(commandName, state.aliases(), state.owner(), state.operator()));
        
        AncapBukkit.registerCommandExecutor(commandName, state.owner(), state.aliases(), this.proxy);
    }

    @Override
    public void unregister(String commandName) {
        CommandHandleState handleState = this.handleStates.get(commandName);
        if (handleState == null) throw new CommandNotRegisteredException(commandName);
        for (String command : this.totalExecutesFor(handleState)) this.redirectMap.remove(command);
        this.handleStates.remove(commandName);
        
        AncapBukkit.unregisterCommandExecutor(commandName);
    }

    private List<String> totalExecutesFor(CommandHandleState handleState) {
        List<String> totalExecutes = new ArrayList<>();
        List<String> aliases = handleState.aliases();
        totalExecutes.add(handleState.commandName());
        totalExecutes.addAll(aliases);
        return totalExecutes;
    }

    @Override
    public void setExecutor(String commandName, CommandOperator operator) throws CommandNotRegisteredException {
        CommandHandleState handleState = this.handleStates.get(commandName);
        if (handleState == null) throw new CommandNotRegisteredException(commandName);
        this.handleStates.put(commandName, handleState.withOperator(operator));
    }

    @Override
    public @Nullable CommandHandleState findRegisterStateOf(String commandName) {
        return this.handleStates.get(commandName);
    }

    @Override
    public void on(CommandDispatch dispatch) {
        this.operate(
            dispatch.command(),
            commandForm -> commandForm.commandOperator.on(new CommandDispatch(
                dispatch.source(),
                commandForm.command
            )),
            message -> Communicator.of(dispatch.source().sender()).message(message)
        );
    }

    @Override
    public void on(CommandWrite write) {
        this.operate(
            write.line(),
            commandForm -> commandForm.commandOperator.on(new CommandWrite(
                write.speaker(),
                commandForm.command
            )),
            message -> Communicator.of(write.speaker().source().sender()).message(message)
        );
    }

    private void operate(LeveledCommand command, Consumer<CommandForm> commandFormConsumer, Consumer<CallableMessage> fallback) {
        String key = command.nextArgument();
        LeveledCommand finalCommand = command.withoutArgument();
        new Thread(() -> {
            try {
                String id = this.redirectMap.get(key);
                CommandOperator rule = this.handleStates.get(id).operator();
                commandFormConsumer.accept(new CommandForm(
                    finalCommand,
                    rule
                ));
            } catch (Throwable throwable) {
                fallback.accept(new LAPIMessage(Artifex.class, "command.api.error.internal"));
                throwable.printStackTrace();
            }
        }).start();

    }

    @Override
    public boolean isOperate(LeveledCommand command) {
        if (command.isRaw()) return false;
        return this.redirectMap.containsKey(command.nextArgument());
    }
    
    private record CommandForm(LeveledCommand command, CommandOperator commandOperator) {}
    
}
