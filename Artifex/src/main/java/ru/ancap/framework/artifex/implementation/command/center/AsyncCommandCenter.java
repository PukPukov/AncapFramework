package ru.ancap.framework.artifex.implementation.command.center;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.command.CommandExecutor;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.map.GuaranteedMap;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.commands.object.conversation.CommandSource;
import ru.ancap.framework.command.api.commands.object.dispatched.LeveledCommand;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandExceptionOperator;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.plugin.api.AncapBukkit;
import ru.ancap.framework.plugin.api.AncapPlugin;
import ru.ancap.framework.plugin.api.commands.CommandCenter;
import ru.ancap.framework.plugin.api.commands.CommandData;
import ru.ancap.framework.plugin.api.commands.CommandHandleState;
import ru.ancap.framework.plugin.api.commands.exception.CommandExceptionCenter;
import ru.ancap.framework.plugin.api.exception.CommandAlreadyRegisteredException;
import ru.ancap.framework.plugin.api.exception.CommandNotRegisteredException;
import ru.ancap.framework.plugin.util.CommandErrorMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class AsyncCommandCenter implements CommandExceptionCenter, CommandCenter, CommandOperator, OperateRule {
    
    private final boolean verboseGenericErrorMessage;
    
    private final Map<String /*command*/, String /*id*/               > redirectMap     = new ConcurrentHashMap<>();
    private final Map<String /*id*/,      CommandData                 > commandData     = new ConcurrentHashMap<>();
    private final Map<Class<?>,           CommandExceptionOperator<?> > exceptionData   = new ConcurrentHashMap<>();
    private final Map<AncapPlugin,        Set<String /*id*/         > > pluginRegisters = new GuaranteedMap<>(ConcurrentHashMap::newKeySet);
    
    private final CommandExecutor proxy;

    @Override
    public void initialize(AncapPlugin plugin) {
        for (String commandName : plugin.settings().getCommandList()) this.register(
            commandName,
            plugin.settings().getAliasesList(commandName),
            new CommandHandleState(
                CommandOperator.EMPTY,
                plugin
            )
        );
    }
    
    @Override
    public void register(String id, List<String> sources, CommandHandleState state) {
        CommandData previous = this.commandData.get(id);
        if (previous != null) throw new CommandAlreadyRegisteredException(id);
        
        for (String redirect : sources) this.redirectMap.put(redirect, id);
        CommandData data = new CommandData(id, sources, state);
        this.commandData.put(id, data);
        
        AncapPlugin owner = state.owner() != null ? state.owner() : Artifex.PLUGIN();
        this.pluginRegisters.get(owner).add(id);
        
        AncapBukkit.registerCommandExecutor(id, owner, sources, this.proxy);
    }

    @Override
    public void unregister(String id) {
        CommandData data = this.commandData.get(id);
        if (data == null) throw new CommandNotRegisteredException(id);
        for (String command : data.sources()) this.redirectMap.remove(command);
        if (data.handleState().owner() != null) this.pluginRegisters.get(data.handleState().owner()).remove(id);
        this.commandData.remove(id);
        
        AncapBukkit.unregisterCommandExecutor(id);
    }
    
    @Override
    public <T extends Throwable> void register(Class<T> exception, CommandExceptionOperator<T> operator) {
        CommandExceptionOperator<T> exceptionOperator = (CommandExceptionOperator<T>) this.exceptionData.get(exception);
        if (exceptionOperator != null) throw new IllegalArgumentException("Exception operator for "+exception.getName()+" already registered");
        this.exceptionData.put(exception, operator);
    }
    
    @Override
    public <T extends Throwable> void unregister(Class<T> exception) {
        CommandExceptionOperator<T> exceptionOperator = (CommandExceptionOperator<T>) this.exceptionData.get(exception);
        if (exceptionOperator == null) throw new IllegalArgumentException("Exception operator for "+exception.getName()+" not found");
        this.exceptionData.remove(exception);
    }

    @Override
    public void setExecutor(String id, CommandOperator operator) throws CommandNotRegisteredException {
        CommandData data = this.commandData.get(id);
        if (data == null) throw new CommandNotRegisteredException(id);
        this.commandData.put(id, data.withHandleState(data.handleState().withOperator(operator)));
    }

    @Override
    public @Nullable CommandData findDataOf(String id) {
        return this.commandData.get(id);
    }

    @Override
    public Set<String> findRegisteredCommandsOf(AncapPlugin plugin) {
        return this.pluginRegisters.get(plugin);
    }

    @Override
    public void on(CommandDispatch dispatch) {
        this.operate(
            dispatch.source(),
            dispatch.command(),
            commandForm -> commandForm.commandOperator.on(new CommandDispatch(
                dispatch.source(),
                commandForm.command
            ))
        );
    }

    @Override
    public void on(CommandWrite write) {
        this.operate(
            write.speaker().source(),
            write.line(),
            commandForm -> commandForm.commandOperator.on(new CommandWrite(
                write.speaker(),
                commandForm.command
            ))
        );
    }

    private void operate(CommandSource source, LeveledCommand command, Consumer<CommandForm> commandFormConsumer) {
        String key = command.nextArgument();
        LeveledCommand finalCommand = command.withoutArgument();
        new Thread(() -> {
            try {
                String id = this.redirectMap.get(key);
                CommandOperator rule = this.commandData.get(id).handleState().operator();
                commandFormConsumer.accept(new CommandForm(
                    finalCommand,
                    rule
                ));
            } catch (Throwable throwable) {
                var target = (CommandExceptionOperator<Throwable>) this.exceptionData.get(throwable.getClass());
                if (target != null) target.on(throwable, source, command);
                else {
                    CommandErrorMessage.send(source.sender(), this.verboseGenericErrorMessage ? new LAPIMessage(
                        Artifex.class, "command.api.error.internal",
                        new Placeholder("exception name", throwable.getClass().getName()),
                        new Placeholder("exception message", throwable.getMessage())
                    ) : new LAPIMessage(Artifex.class, "command.api.error.internal"));
                    throwable.printStackTrace();
                }
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