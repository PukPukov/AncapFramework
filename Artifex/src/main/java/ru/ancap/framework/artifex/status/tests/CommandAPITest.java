package ru.ancap.framework.artifex.status.tests;

import ru.ancap.framework.artifex.status.tests.util.TestCommandRegistration;
import ru.ancap.framework.command.api.commands.object.executor.CommandOperator;
import ru.ancap.framework.command.api.commands.operator.delegate.Delegate;
import ru.ancap.framework.command.api.commands.operator.delegate.subcommand.SubCommand;
import ru.ancap.framework.plugin.api.commands.PluginCommandRegistrar;
import ru.ancap.framework.status.test.HandTest;
import ru.ancap.framework.status.test.PlayerTest;
import ru.ancap.framework.status.util.Tester;

import static ru.ancap.framework.artifex.status.tests.util.Util.commandQuestion;

public class CommandAPITest extends PlayerTest implements HandTest {
    
    public CommandAPITest(PluginCommandRegistrar registrar) {
        super(
            "command-api",
            (player, tester) -> {
                testDelegateNotEnoughArguments(registrar, tester);
                return TestResult.SUCCESS;
            }
        );
    }
    
    private static void testDelegateNotEnoughArguments(PluginCommandRegistrar registrar, Tester tester) {
        String commandKey = "delegated-command";
        CommandOperator testOperator = new Delegate(
            new SubCommand("foo", dispatch -> {})
        );
        
        try (var __ = TestCommandRegistration.register(registrar, commandKey, testOperator)) {
            tester.askQuestion(commandQuestion("error", "interact", commandKey, (byte) -1));
        }
    }
    
}