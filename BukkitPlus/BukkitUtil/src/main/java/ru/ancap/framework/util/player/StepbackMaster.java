package ru.ancap.framework.util.player;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ancap.commons.map.MapGC;
import ru.ancap.commons.map.SafeMap;
import ru.ancap.commons.null_.SafeNull;
import ru.ancap.framework.identifier.Identifier;

import java.util.Queue;
import java.util.function.Predicate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StepbackMaster {
    
    private final SafeMap<String, Queue<Location>> data;
    private final JavaPlugin owner;
    private final int thinning;
    
    // TODO make sure StepbackMaster is working and still needed and if not nuke it
    public StepbackMaster(JavaPlugin owner, MapGC<String> identifierMapGC, int thinning, int maxLocationsSaved) {
        this(
            SafeMap.<String, Queue<Location>>builder()
                .guaranteed(() -> new CircularFifoQueue<>(maxLocationsSaved))
                .collectGarbage(identifierMapGC)
                .build(),
            owner,
            thinning
        );
    }
    
    public void run() {
        Bukkit.getScheduler().runTaskTimer(this.owner, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                StepbackMaster.this.data.get(Identifier.of(player)).add(player.getLocation());
            });
        }, 100, 20L * this.thinning);
    }
    
    public void make(Player player) {
        SafeNull.action(this.data.get(Identifier.of(player)).poll(), player::teleport);
    }
    
    public void makeUntil(Player player, Predicate<Location> acceptanceChecker, Runnable ifCantFindMatching) {
        @NotNull Location location;
        do {
            @Nullable Location nullableLastLocation = this.data.get(Identifier.of(player)).poll();
            if (nullableLastLocation == null) { ifCantFindMatching.run(); return; }
            location = nullableLastLocation;
        } while (!acceptanceChecker.test(location));
        SafeNull.action(location, player::teleport);
    }
    
    public void ensureOuter(Player player, Predicate<Location> regionPredicate, Runnable fallback) {
        if (regionPredicate.test(player.getLocation())) return;
        else this.makeUntil(player, regionPredicate, fallback);
    }
    
}