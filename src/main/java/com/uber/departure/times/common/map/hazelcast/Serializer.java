package com.uber.departure.times.common.map.hazelcast;

import java.util.Collection;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public interface Serializer<V, S> {
    @NotNull
    S to(@NotNull V value);

    @NotNull
    V from(@NotNull S value);

    @NotNull
    default Collection<S> to(@NotNull Collection<V> value) {
        return value.stream().map(this::to).collect(Collectors.toList());
    }

    @NotNull
    default Collection<V> from(@NotNull Collection<S> value) {
        return value.stream().map(this::from).collect(Collectors.toList());
    }
}
