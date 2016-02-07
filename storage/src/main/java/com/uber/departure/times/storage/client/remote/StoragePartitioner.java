package com.uber.departure.times.storage.client.remote;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;
import com.uber.departure.times.common.server.cluster.Partitioner;
import com.uber.departure.times.storage.client.grid.GridCoordinates;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class StoragePartitioner implements Partitioner {
    @Override
    public int hash(@NotNull Object key) {
        Objects.requireNonNull(key, "key");
        if (key instanceof GridCoordinates) {
            final GridCoordinates coordinates = (GridCoordinates) key;
            return Hashing.murmur3_32()
                    .hashObject(
                            coordinates,
                            (c, to) -> to.putInt(c.getX()).putInt(c.getY())
                    ).asInt();
        }
        throw new UnsupportedOperationException(key.getClass().getCanonicalName());
    }
}
