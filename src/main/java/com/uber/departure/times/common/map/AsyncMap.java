package com.uber.departure.times.common.map;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.Future;

/**
 * @author Danila Ponomarenko
 */
public interface AsyncMap<K, V> {
    @NotNull
    Future<V> get(@NotNull K key);

    @NotNull
    Future<V> put(@NotNull K key, @NotNull V value, long ttlMs);

    @NotNull
    Future<V> computeIfAbsent(@NotNull K key, @NotNull Function<K, Future<V>> f, long ttlMs);
}
