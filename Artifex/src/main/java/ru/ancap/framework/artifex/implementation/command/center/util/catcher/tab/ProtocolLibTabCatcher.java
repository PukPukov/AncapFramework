package ru.ancap.framework.artifex.implementation.command.center.util.catcher.tab;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.utility.MinecraftVersion;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

@ToString @EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true) @Getter
public class ProtocolLibTabCatcher implements PacketListener {
    
    @Delegate
    private final PacketListener delegate;
    
    private final CommonTabCatcher commonTabCatcher;
    
    private boolean versionCacheSetup;
    private boolean versionCache;
    
    private final Consumer<PacketEvent> onTabComplete = (event) -> {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        PacketContainer packet = event.getPacket();
        
        String text = packet.getStrings().read(0);
        int transactionID = this.readTransactionId() ? packet.getIntegers().read(0) : 0;
        
        this.commonTabCatcher().handle(player,
            () -> transactionID,
            () -> text,
            () -> event.setCancelled(true)
        );
    };
    
    private boolean readTransactionId() {
        if (!this.versionCacheSetup) {
            this.versionCache = ProtocolLibrary.getProtocolManager().getMinecraftVersion().compareTo(new MinecraftVersion("1.13")) >= 0;
            this.versionCacheSetup = true;
        }
        return this.versionCache;
    }
    
    public ProtocolLibTabCatcher(
        JavaPlugin plugin, CommonTabCatcher commonTabCatcher
    ) {
        this.delegate = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                onTabComplete.accept(event);
            }
        };
        this.commonTabCatcher = commonTabCatcher;
    }
    
}