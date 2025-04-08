package ru.ancap.framework.artifex.status.tests;

import org.bukkit.entity.Player;
import ru.ancap.framework.artifex.status.tests.util.TestCommandRegistration;
import ru.ancap.framework.command.api.commands.object.event.CommandDispatch;
import ru.ancap.framework.command.api.commands.object.event.CommandWrite;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.object.tab.Tab;
import ru.ancap.framework.command.api.commands.object.tab.TabBundle;
import ru.ancap.framework.command.api.commands.object.tab.TooltipTab;
import ru.ancap.framework.communicate.message.Text;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.communicate.util.CMMSerializer;
import ru.ancap.framework.plugin.api.AncapBukkit;
import ru.ancap.framework.plugin.api.commands.PluginCommandRegistrar;
import ru.ancap.framework.status.InteractionRequest;
import ru.ancap.framework.status.test.HandTest;
import ru.ancap.framework.status.test.PlayerTest;
import ru.ancap.framework.status.util.TestIdentity;
import ru.ancap.framework.status.util.Tester;

import java.util.List;

import static ru.ancap.framework.artifex.status.tests.util.Util.baseResponse;
import static ru.ancap.framework.artifex.status.tests.util.Util.commandQuestion;

public class CommandCenterTest extends PlayerTest implements HandTest {
    
    public CommandCenterTest(PluginCommandRegistrar registrar) {
        super(
            "command-center",
            (player, tester) -> {
                testProxy              (registrar, player, tester);
                testRegisterUnregister (registrar, tester);
                testTabCompletions     (registrar, tester);
                testUnknownError       (registrar, tester);
                return TestResult.SUCCESS;
            }
        );
    }
    
    private static void testProxy(PluginCommandRegistrar registrar, Player player, Tester tester) {
        String commandKey = "some-command-to-proxy";
        byte identity = TestIdentity.get();
        CommandOperator testOperator = baseResponse("test.ok-response", identity);
        
        try (var __ = TestCommandRegistration.register(registrar, commandKey, testOperator)) {
            AncapBukkit.sendCommand(player, commandKey);
            tester.askQuestion(commandQuestion("ok", "inspect", commandKey, identity));
        }
    }
    
    private static void testTabCompletions(PluginCommandRegistrar registrar, Tester tester) {
        String commandKey = "tab-completed-command";
        String gradientMinimessage = "<gradient:#0400eb:#f00000>ffffiiiizzzzzzzz</gradient>";
        CommandOperator testOperator = new CommandOperator() {
            @Override
            public void on(CommandWrite write) {
                write.speaker().sendTab(TabBundle.builder()
                    .tooltiped(List.of(
                        new TooltipTab("foo", CMMSerializer.serialize(gradientMinimessage)),
                        new Tab("bar")
                    ))
                    .build()
                );
            }
            
            @Override public void on(CommandDispatch dispatch) {}
        };
        
        try (var __ = TestCommandRegistration.register(registrar, commandKey, testOperator)) {
            tester.askQuestion(new InteractionRequest(
                "tab-complete", List.of(new Placeholder("command", commandKey)),
                "gradient-tab-complete", List.of(new Placeholder("gradient", new Text(gradientMinimessage)))
            ));
        }
    }
    
    private static void testRegisterUnregister(PluginCommandRegistrar registrar, Tester tester) {
        String commandKey = "example-command-for-test";
        byte identity = TestIdentity.get();
        CommandOperator testOperator = baseResponse("test.ok-response", identity);
        
        try (var __ = TestCommandRegistration.register(registrar, commandKey, testOperator)) {
            tester.askQuestion(commandQuestion("ok", "interact", commandKey, identity));
        }
        
        tester.askQuestion(commandQuestion("unknown", "interact", commandKey, identity));
    }
    
    private static void testUnknownError(PluginCommandRegistrar registrar, Tester tester) {
        String commandKey = "unknown-error-command";
        CommandOperator testOperator = dispatch -> {
            throw new IllegalStateException("This is fine.");
        };
        
        try (var __ = TestCommandRegistration.register(registrar, commandKey, testOperator)) {
            tester.askQuestion(commandQuestion("error", "interact", commandKey, (byte) -1));
        }
    }

}