package ru.ancap.framework.artifex.implementation.command.communicate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.ancap.framework.runtime.Artifex;
import ru.ancap.framework.command.api.event.OperableEvent;
import ru.ancap.framework.command.api.event.classic.*;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.message.CallableMessage;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;

import static org.bukkit.event.EventPriority.*;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class PlayerCommandFallback implements Listener {

    @EventHandler(priority = NORMAL)
    public void on(UnknownCommandEvent event) {
        this.operateForm(
            new Form(event, event.sender()),
            new LAPIMessage(
                Artifex.class, "command.api.error.unknown",
                new Placeholder("COMMAND", event.unknownValue())
            )
        );
    }

    @EventHandler(priority = NORMAL)
    public void on(NotEnoughArgumentsEvent event) {
        this.operateForm(
            new Form(event, event.sender()),
            new LAPIMessage(
                Artifex.class, "command.api.error.not-enough-arguments",
                new Placeholder("COUNT", String.valueOf(event.argumentsLack()))
            )
        );
    }

    @EventHandler(priority = NORMAL)
    public void on(NotEnoughPermissionsEvent event) {
        this.operateForm(
            new Form(event, event.sender()),
            new LAPIMessage(
                Artifex.class, "command.api.error.not-enough-permissions"
            )
        );
    }
    
    @EventHandler(priority = NORMAL)
    public void on(CannotTransformArgumentEvent event) {
        this.operateForm(
            new Form(event, event.sender()),
            new LAPIMessage(
                Artifex.class, "command.api.error.cannot-transform-argument",
                new Placeholder("ARGUMENT", String.join(" ", event.argument())),
                new Placeholder("TYPE", new LAPIMessage("ru.ancap.types."+event.type().getName()))
            )
        );
    }
    
    @EventHandler(priority = NORMAL)
    public void on(UnexecutableCommandEvent event) {
        this.operateForm(new Form(event, event.sender()), event.description());
    }

    private void operateForm(Form form, CallableMessage message) {
        if (form.event().operate()) {
            CommandSender sender = form.sender();
            if (sender instanceof Player player) {
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 2, 0);
            }
            Communicator.of(sender).message(message);
        }
    }

    private record Form(OperableEvent event, CommandSender sender) {}

}