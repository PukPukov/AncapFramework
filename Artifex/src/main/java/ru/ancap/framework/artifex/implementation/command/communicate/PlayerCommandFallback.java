package ru.ancap.framework.artifex.implementation.command.communicate;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.ancap.communicate.Communicator;
import ru.ancap.communicate.replacement.Placeholder;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.command.api.event.OperableEvent;
import ru.ancap.framework.command.api.event.classic.*;
import ru.ancap.framework.language.additional.LAPIMessage;

@AllArgsConstructor
public class PlayerCommandFallback implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(UnknownCommandEvent event) {
        this.operateForm(
                new Form(event, event.sender()),
                "command.api.error.unknown",
                new Placeholder("COMMAND", event.unknownValue())
        );
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(NotEnoughArgumentsEvent event) {
        this.operateForm(
                new Form(event, event.sender()),
                "command.api.error.not-enough-arguments",
                new Placeholder("COUNT", String.valueOf(event.argumentsLack()))
        );
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(NotEnoughPermissionsEvent event) {
        this.operateForm(
                new Form(event, event.sender()),
                "command.api.error.not-enough-permissions",
                new Placeholder("PERMISSION", event.lackedPermission())
        );
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(DescribedIncorrectArgumentsEvent event) {
        this.operateForm(
                new Form(event, event.sender()),
                event.description()
        );
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void on(CannotTransformArgumentEvent event) {
        this.operateForm(
                new Form(event, event.sender()),
                "command.api.error.cannot-transform-argument", 
                new Placeholder("ARGUMENT", String.join(" ", event.argument())),
                new Placeholder("TYPE", event.type())
        );
    }

    private void operateForm(Form form, String id, Placeholder... placeholders) {
        if (form.getEvent().operate()) {
            CommandSender sender = form.getSender();
            if (sender instanceof Player) {
                Player player = (Player) sender; 
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 2, 0);
            }
            new Communicator(sender).send(new LAPIMessage(
                    Artifex.class, "command.api.error.operate-is-impossible",
                    new Placeholder("description", new LAPIMessage(Artifex.class, id, placeholders))
            ));
        }
    }

    @AllArgsConstructor
    @Data
    private static class Form {
        
        private final OperableEvent event;
        private final CommandSender sender;
        
    }

}
