package ru.ancap.framework.status.test;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.ancap.framework.communicate.communicator.Communicator;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;
import ru.ancap.framework.status.util.Tester;

public class PlayerTest extends AbstractTest {

    public PlayerTest(String nameId, PlayerTest.Function test) {
        super(nameId, (identifier) -> {
            Player player = Bukkit.getPlayer(identifier);
            Tester tester = new Tester(Communicator.of(player));
            if (player == null) return TestResult.skip(new LAPIMessage(
                CommonMessageDomains.Status.Skip.notThatTester,
                new Placeholder("required", new LAPIMessage(CommonMessageDomains.Status.Skip.testerTypes+".player"))
            ));
            return test.run(player, tester);
        });
    }
    
    public interface Function {
        
        TestResult run(Player player, Tester tester);
        
    }
    
}