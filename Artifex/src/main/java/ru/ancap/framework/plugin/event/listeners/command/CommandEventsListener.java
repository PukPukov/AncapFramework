package ru.ancap.framework.plugin.event.listeners.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.ancap.framework.api.LAPI;
import ru.ancap.framework.api.command.util.TypeNameProvider;
import ru.ancap.framework.api.event.OperableEvent;
import ru.ancap.framework.api.event.classic.*;
import ru.ancap.framework.plugin.Artifex;
import ru.ancap.util.communicate.Communicator;

@AllArgsConstructor
public class CommandEventsListener implements Listener {

    private final TypeNameProvider typeNameProvider;

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(UnknownCommandEvent event) {
        this.operateForm(
                new Form(event, event.getSender()),
                Artifex.MESSAGE_DOMAIN+"unknown-command",
                new Placeholder("COMMAND", event.getUnknownValue())
        );
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(NotEnoughArgumentsEvent event) {
        this.operateForm(
                new Form(event, event.getSender()),
                Artifex.MESSAGE_DOMAIN+"not-enough-arguments",
                new Placeholder("COUNT", String.valueOf(event.getArgumentsLack()))
        );
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(NotEnoughPermissionsEvent event) {
        this.operateForm(
                new Form(event, event.getSender()),
                Artifex.MESSAGE_DOMAIN+"not-enough-permissions",
                new Placeholder("PERMISSION", event.getLackedPermission())
        );
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(DescribedIncorrectArgumentsEvent event) {
        this.operateForm(
                new Form(event, event.getSender()),
                event.getDescription()
        );
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(IncorrectArgumentEvent event) {
        this.operateForm(
                new Form(event, event.getSender()),
                Artifex.MESSAGE_DOMAIN+"incorrect-argument",
                new Placeholder("ARGUMENT", event.getArgument())
        );
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void on(CannotTransformArgumentEvent event) {
        this.operateForm(
                new Form(event, event.getSender()),
                Artifex.MESSAGE_DOMAIN+"cannot-transform-argument", 
                new Placeholder("ARGUMENT", event.getArgument()),
                new Placeholder("TYPE", typeNameProvider.apply(event.getType(), event.getSender()))
        );
    }

    private void operateForm(Form form, String id, Placeholder... placeholders) {
        if (form.getEvent().operate()) {
            CommandSender sender = form.getSender();
            if (sender instanceof Player) {
                Player player = (Player) sender; 
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 2, 0);
            }
            String mainMessage = LAPI.localized(Artifex.MESSAGE_DOMAIN+"command-operate-is-impossible", sender.getName());
            String message = LAPI.localized(id, sender.getName());
            for (Placeholder placeholder : placeholders) {
                message = message.replace(placeholder.getPlaceholder(), placeholder.replaceTo);
            }
            mainMessage = mainMessage.replace("%DESCRIPTION%", message);
            new Communicator(sender).send(mainMessage);
        }
    }

    @AllArgsConstructor
    @Data
    private static class Form {
        private final OperableEvent event;
        private final CommandSender sender;
    }

    @Data
    private static class Placeholder {
        private final String placeholder;
        private final String replaceTo;
        
        private Placeholder(String placeholder, String replaceTo) {
            this.placeholder = "%" + placeholder + "%";
            this.replaceTo = replaceTo;
        }

    }

}
