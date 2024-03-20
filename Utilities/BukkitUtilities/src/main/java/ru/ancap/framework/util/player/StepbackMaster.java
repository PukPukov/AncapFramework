package ru.ancap.framework.util.player;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.material.Step;
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
    
    preventcompile;
    /*
     подумать над тем, куда запихнуть степбек мастер
     с одной стороны это не интероп и его можно шейдить, но лучше всего его запускать в единичном экземпляре из-за того, что он нагружает сервер
     (не то, чтобы нагружает сильно, но умножать количество обработчиков степбеков на количество плагинов не лучшая идея)
     что насчёт статик инициализатора + шейдинга по номеру версии? или подключения через реализацию, зашейженной по номеру версии? 
     в таком случае будет создаваться по каждому экземляру мастера на каждую версию всего фреймворка, когда в теории необходимо разделять его только
     тогда, когда в этом есть смысл. Добавлять к каждому классу пакет с версией этого самого класса и изменять его при брекинг ченджах в нём?
     пока что я думаю стоит остановиться на шейдинге по номеру версии. Потом, если необходимость появиться, можно изменить это на что-то более умное,
     всё равно любые брекинг ченджи не будут поломкой совместимости в рантайме за счёт уже существующего шейдинга, и единственной проблемой останется
     повышенная нагрузка на сервер от старых обработчиков, которые можно без проблем оптимизировать будет обновлением на новую версию
     */
    
    private final SafeMap<String, Queue<Location>> data;
    private final JavaPlugin owner;
    private final int thinning;
    
    public static StepbackMaster init(JavaPlugin owner, MapGC<String> identifierMapGC, int thinning, int maxLocationsSaved) {
        var master = new StepbackMaster(owner, identifierMapGC, thinning, maxLocationsSaved);
        master.run();
        return master;
    }
    
    private StepbackMaster(JavaPlugin owner, MapGC<String> identifierMapGC, int thinning, int maxLocationsSaved) {
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
        Bukkit.getScheduler().runTaskTimer(this.owner, () -> Bukkit.getOnlinePlayers().forEach(player -> {
            StepbackMaster.this.data.get(Identifier.of(player)).add(player.getLocation());
        }), 100, 20L * this.thinning);
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