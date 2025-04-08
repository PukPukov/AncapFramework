package ru.ancap.framework.plugin.api.information;

import lombok.AllArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableText;
import ru.ancap.framework.communicate.message.Text;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIText;
import ru.ancap.framework.speak.common.CommonMessageDomains;

@AllArgsConstructor
public class AuthorsSupplier implements CommandOperator {
    
    private final JavaPlugin plugin;
    private final String domain;

    public AuthorsSupplier(JavaPlugin plugin) {
        this(plugin, CommonMessageDomains.pluginInfo);
    }

    @Override
    public void on(CommandDispatch dispatch) {
        Communicator.of(dispatch.source().sender()).message(new LAPIText(
                this.domain,
                new Placeholder("plugin", this.plugin.getName()),
                new Placeholder("version", new Text(this.plugin.getDescription().getVersion())),
                new Placeholder("authors", this.plugin.getDescription().getAuthors().stream().reduce((s1, s2) -> s1+", "+s2)
                        .map(authors -> (CallableText) new Text(authors))
                        .orElse(new LAPIText("literals.nobody"))
                )
        ));
    }
    
}