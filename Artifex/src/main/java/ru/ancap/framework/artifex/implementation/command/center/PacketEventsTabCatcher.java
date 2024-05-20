package ru.ancap.framework.artifex.implementation.command.center;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;
import lombok.*;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;

@ToString @EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Accessors(fluent = true) @Getter
public class PacketEventsTabCatcher implements PacketListener {

    private final CommonTabCatcher commonTabCatcher;
    
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getPacketType() != PacketType.Play.Client.TAB_COMPLETE) return;
        var packet = new WrapperPlayClientTabComplete(event);
        if (event.isCancelled()) return;
        
        //noinspection Convert2MethodRef to save unified lambda style
        this.commonTabCatcher.handle(player,
            () -> packet.getTransactionId().orElse(0),
            () -> packet.getText(),
            () -> event.setCancelled(true)
        );
    }
    
}