package ru.ancap.framework.artifex.implementation.communicator.message.clickable;

import lombok.RequiredArgsConstructor;
import ru.ancap.commons.map.GuaranteedMap;
import ru.ancap.framework.command.api.commands.object.dispatched.LCParseState;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.message.Text;
import ru.ancap.framework.communicate.message.clickable.ActionMessageProvider;
import ru.ancap.framework.communicate.message.clickable.Click;
import ru.ancap.framework.communicate.message.clickable.ClickableText;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.plugin.api.commands.PluginCommandRegistrar;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ActionProxy implements CommandOperator, ActionMessageProvider {

    private final Map<String, Consumer<Click>> proxyMap = new GuaranteedMap<>(() -> click -> {});
    private final String commandName;

    public void setup(PluginCommandRegistrar registrar) {
        ClickableText.provider = this;
        registrar.register(this.commandName, List.of(), this);
    }
    
    @Override
    public CallableText to(CallableText base, Consumer<Click> clickConsumer) {
        String actionId = generateActionID();
        this.proxyMap.put(actionId, clickConsumer);
        return new Text(
            "<click:run_command:/"+this.commandName+" "+actionId+">%AP_TEXT%</click>",
            new Placeholder("ap text", base)
        );
    }

    @Override
    public void on(CommandDispatch dispatch) {
        LCParseState onActionID = dispatch.command().step();
        this.proxyMap.get(onActionID.part().main()).accept(new Click(dispatch.source().sender()));
        this.proxyMap.remove(onActionID.part().main());
    }

    private long actionCounter = 0;

    public String generateActionID() {
        try { return actionCounter+""; }
        finally { actionCounter++; }
    }
    
}