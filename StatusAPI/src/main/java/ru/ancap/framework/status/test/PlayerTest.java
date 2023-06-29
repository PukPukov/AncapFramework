package ru.ancap.framework.status.test;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.ancap.framework.communicate.modifier.Placeholder;
import ru.ancap.framework.language.additional.LAPIMessage;
import ru.ancap.framework.speak.common.CommonMessageDomains;

import java.util.function.Function;

public class PlayerTest extends AbstractTest {

    public PlayerTest(String nameId, Function<Player, TestResult> test) {
        super(nameId, (identifier) -> {
            Player player = Bukkit.getPlayer(identifier);
            if (player == null) return TestResult.skip(new LAPIMessage(
                CommonMessageDomains.Status.Skip.notThatTester,
                new Placeholder("required", new LAPIMessage(CommonMessageDomains.Status.Skip.testerTypes+".player"))
            ));
            return test.apply(player);
        });
    }
    
}
