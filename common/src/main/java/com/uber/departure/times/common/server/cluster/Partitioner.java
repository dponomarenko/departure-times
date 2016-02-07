package com.uber.departure.times.common.server.cluster;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public interface Partitioner {
    int hash(@NotNull Object key);

    default byte partition(int hash) {
        return (byte) (hash & 0xff
                ^ hash & (0xff << 8)
                ^ hash & (0xff << 16)
                ^ hash & (0xff << 24));
    }

    default byte partition(@NotNull Object key) {
        return partition(hash(key));
    }
}
