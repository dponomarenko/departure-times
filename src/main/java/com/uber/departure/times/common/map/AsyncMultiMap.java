package com.uber.departure.times.common.map;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.Future;

/**
 * @author Danila Ponomarenko
 */
public interface AsyncMultiMap<K, V> {
    @NotNull
    Future<Collection<V>> get(@NotNull K key);

    @NotNull
    Future<Boolean> add(@NotNull K key, @NotNull V value);
}
