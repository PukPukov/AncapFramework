package ru.ancap.framework.api.center.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.Utility;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class OwnershipResolvingCommandRegistrar {
    
    private final CommandCenter commandCenter;
    
    public abstract CommandRegistrationBuilder register(Owner owner, String id);
    
    public abstract Optional<ResolvedCRS> read(String id);
    public abstract List<ResolvedCRS> readAll();
    public abstract List<ResolvedCRS> ownedBy(Owner owner);
    
    @Utility public void unload(Owner owner) {
        for (var rcrs : this.ownedBy(owner).reversed()) {
            this.commandCenter.unregister(rcrs.crs().id());
        }
    }
    
    public record Owner(String id) { }
    public record ResolvedCRS(CommandRegistrationState crs, Owner owner) { }
    
}