package ru.ancap.framework.api.command.commands.operator.speaking;

import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import ru.ancap.framework.api.command.commands.command.event.CommandDispatch;
import ru.ancap.framework.api.command.commands.command.executor.CommandOperator;
import ru.ancap.util.communicate.Communicator;

@AllArgsConstructor
public class Adviser implements CommandOperator {

    private AdviseProvider provider;

    @Override
    public void on(CommandDispatch dispatch) {
        new Communicator(dispatch.sender()).send(this.provider.advice(dispatch.sender()));
    }

    public interface AdviseProvider {

        String advice(CommandSender sender);

    }
    
}
