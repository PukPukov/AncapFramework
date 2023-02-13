package ru.ancap.framework.api.world.iterator;

import lombok.AllArgsConstructor;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import ru.ancap.lib.scalar.containers.*;

import java.util.Iterator;

@AllArgsConstructor
public class ChunkIterator implements Iterable<Block> {

    private final Chunk chunk;

    @NotNull
    @Override
    public Iterator<Block> iterator() {
        return new ContainerIterator<>(
                DiscretePositionContainer
                        .builder()
                        .add(DiscreteRange.of(Axis.X, 0, 15))
                        .add(DiscreteRange.of(Axis.Y, 0, 15))
                        .add(DiscreteRange.of(Axis.Z, chunk.getWorld().getMinHeight(), chunk.getWorld().getMaxHeight()))
                        .build(),
                discretePosition -> chunk.getBlock(
                        (int) discretePosition.coordinate(Axis.X),
                        (int) discretePosition.coordinate(Axis.Z),
                        (int) discretePosition.coordinate(Axis.Y)
                )
        ).iterator();
    }
}
