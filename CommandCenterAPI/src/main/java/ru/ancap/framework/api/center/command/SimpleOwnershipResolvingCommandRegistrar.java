package ru.ancap.framework.api.center.command;

import java.util.List;
import java.util.Optional;

public class SimpleOwnershipResolvingCommandRegistrar extends OwnershipResolvingCommandRegistrar {
    
    public SimpleOwnershipResolvingCommandRegistrar(CommandCenter commandCenter) {
        super(commandCenter);
    }
    
    @Override
    public CommandRegistrationBuilder register(Owner owner, String id) {
        return null;
    }
    
    @Override
    public Optional<ResolvedCRS> read(String id) {
        return Optional.empty();
    }
    
    @Override
    public List<ResolvedCRS> readAll() {
        return null;
    }
    
    @Override
    public List<ResolvedCRS> ownedBy(Owner owner) {
        return null;
    }
    
}