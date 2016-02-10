package com.uber.departure.times.common.map.hazelcast;

import java.util.Collection;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.hazelcast.core.MultiMap;
import com.uber.departure.times.common.map.AsyncMultiMap;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * @author Danila Ponomarenko
 */
public final class HazelcastMultiMap<K, KM, V, VM> implements AsyncMultiMap<K, V> {
    private final Vertx vertx;
    private final MultiMap<KM, VM> multimap;
    private final Serializer<K, KM> keySerializer;
    private final Serializer<V, VM> valueSerializer;

    public HazelcastMultiMap(@NotNull Vertx vertx,
                             @NotNull MultiMap<KM, VM> multimap,
                             @NotNull Serializer<K, KM> keySerializer,
                             @NotNull Serializer<V, VM> valueSerializer) {
        this.multimap = Objects.requireNonNull(multimap, "multimap");
        this.vertx = Objects.requireNonNull(vertx, "vertx");
        this.keySerializer = Objects.requireNonNull(keySerializer, "keySerializer");
        this.valueSerializer = Objects.requireNonNull(valueSerializer, "valueSerializer");
    }

    @NotNull
    @Override
    public Future<Collection<V>> get(@NotNull K key) {
        Objects.requireNonNull(key, "key");
        final Future<Collection<V>> result = Future.future();
        vertx.executeBlocking(event -> result.complete(get(key, multimap)), result.completer());
        return result;
    }

    @NotNull
    @Override
    public Future<Boolean> add(@NotNull K key, @NotNull V value) {
        Objects.requireNonNull(key, "key");
        final Future<Boolean> result = Future.future();
        vertx.executeBlocking(event -> result.complete(add(key, value, multimap)), result.completer());
        return result;
    }

    @NotNull
    private Collection<V> get(@NotNull K key, @NotNull MultiMap<KM, VM> map) {
        return valueSerializer.from(map.get(keySerializer.to(key)));
    }

    private boolean add(@NotNull K key, @NotNull V value, @NotNull MultiMap<KM, VM> map) {
        return map.put(keySerializer.to(key), valueSerializer.to(value));
    }
}
