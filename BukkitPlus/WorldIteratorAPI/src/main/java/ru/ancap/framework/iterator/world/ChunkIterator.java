package ru.ancap.framework.iterator.world;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import ru.ancap.lib.scalar.containers.Axis;
import ru.ancap.lib.scalar.containers.ContainerIterator;
import ru.ancap.lib.scalar.containers.DiscretePositionContainer;
import ru.ancap.lib.scalar.containers.DiscreteRange;

import java.util.Iterator;

@AllArgsConstructor
@ToString @EqualsAndHashCode
public class ChunkIterator implements Iterable<Block> {

    private final Chunk chunk;

    @NonNull
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
