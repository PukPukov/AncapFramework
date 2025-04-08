package ru.ancap.framework.artifex.status.tests;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ancap.commons.exception.uewrapper.USupplier;
import ru.ancap.framework.artifex.Artifex;
import ru.ancap.framework.status.test.AbstractTest;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainListenerAutoregisterTest extends AbstractTest {
    
    public MainListenerAutoregisterTest(JavaPlugin runner) {
        super(
            "main-listener-autoregister",
            new USupplier<>(() -> {
                var testEvent = new Artifex.MainListenerAutoregisterTestEvent();
                var blocker = new CountDownLatch(1);
                Bukkit.getScheduler().runTask(runner, () -> {
                    Bukkit.getPluginManager().callEvent(testEvent);
                    blocker.countDown();
                });
                blocker.await();
                assertTrue(Artifex.mainListenerAutoregisterTestEventAccepted);
                return TestResult.SUCCESS;
            })
        );
    }
    
}