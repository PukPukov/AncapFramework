package ru.ancap.framework.artifex.implementation.command.center;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import ru.ancap.framework.artifex.implementation.command.object.PacketCommandWrite;
import ru.ancap.framework.command.api.commands.object.dispatched.InlineTextCommand;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.rule.delegate.operate.OperateRule;
import ru.ancap.framework.identifier.Identifier;

import java.util.function.Supplier;

@ToString @EqualsAndHashCode
@RequiredArgsConstructor
@Accessors(fluent = true) @Getter
public class CommonTabCatcher {
    
    private final TabPerformanceChecker performanceChecker;
    private final CommandOperator global;
    private final OperateRule scope;
    
    public void handle(
        Player player,
        Supplier<Integer> transactionIdSupplier,
        Supplier<String> textSupplier,
        Runnable packetCanceller
    ) {
        if (this.performanceChecker.skipFor(() -> Identifier.of(player))) return;
        
        int transactionID = transactionIdSupplier.get();
        String text = textSupplier.get();
        
        InlineTextCommand inlineTextCommand = new InlineTextCommand(text);
        if (!this.scope().isOperate(inlineTextCommand)) return;
        packetCanceller.run();
        this.global().on(new PacketCommandWrite(transactionID, inlineTextCommand, player));
    }
    
}