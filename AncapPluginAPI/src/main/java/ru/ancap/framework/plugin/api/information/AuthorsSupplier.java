package ru.ancap.framework.plugin.api.information;

import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CSCommandOperator;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.message.Message;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;

@AllArgsConstructor
public class AuthorsSupplier implements CSCommandOperator {
    
    private final JavaPlugin plugin;
    private final String domain;

    public AuthorsSupplier(JavaPlugin plugin) {
        this(plugin, CommonMessageDomains.pluginInfo);
    }

    @Override
    public void on(CommandDispatch dispatch) {
        Communicator.of(dispatch.source().sender()).message(new LAPIMessage(
                this.domain,
                new Placeholder("version", new Message(this.plugin.getDescription().getVersion())),
                new Placeholder("authors", this.plugin.getDescription().getAuthors().stream().reduce((s1, s2) -> s1+", "+s2)
                        .map(authors -> (CallableMessage) new Message(authors))
                        .orElse(new LAPIMessage("literals.nobody"))
                )
        ));
    }
    
}
