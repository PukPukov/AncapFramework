package ru.ancap.framework.artifex.status.tests;

import org.bukkit.entity.Player;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.plugin.api.AncapBukkit;
import ru.ancap.framework.plugin.api.commands.PluginCommandRegistrar;
import ru.ancap.framework.status.util.TestDomain;
import ru.ancap.framework.status.util.TestIdentity;
import ru.ancap.framework.status.util.Tester;
import ru.ancap.framework.status.test.PlayerTest;

import java.util.List;

import static ru.ancap.framework.artifex.status.tests.Util.baseResponse;
import static ru.ancap.framework.artifex.status.tests.Util.commandQuestion;

public class CommandCenterTest extends PlayerTest {
    
    public CommandCenterTest(PluginCommandRegistrar registrar) {
        super(
            TestDomain.of(Artifex.class, "command-center"),
            player -> {
                Tester tester = new Tester(Communicator.of(player));
                testRegisterUnregister (registrar, tester);
                testProxy              (registrar, player, tester);
                return TestResult.SUCCESS;
            }
        );
    }

    private static void testProxy(PluginCommandRegistrar registrar, Player player, Tester tester) {
        String commandName = "some-command-to-proxy";
        byte identity = TestIdentity.get();
        
        registrar.register(commandName, List.of(), baseResponse("test.ok-response", identity));
        AncapBukkit.sendCommand(player, commandName);
        tester.askQuestion(commandQuestion("ok", "inspect", commandName, identity));
        
        registrar.unregister(commandName);
    }

    private static void testRegisterUnregister(PluginCommandRegistrar registrar, Tester tester) {
        String commandName = "example-command-for-test";
        byte identity = TestIdentity.get();
        
        registrar.register(commandName, List.of(), baseResponse("test.ok-response", identity));
        tester.askQuestion(commandQuestion("ok", "interact", commandName, identity));

        registrar.unregister(commandName);
        tester.askQuestion(commandQuestion("unknown", "interact", commandName, identity));
    }

}
