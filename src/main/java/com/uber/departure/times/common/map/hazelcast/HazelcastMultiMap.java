package com.uber.departure.times.common.map.hazelcast;

import java.util.Collection;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import com.uber.departure.times.common.map.AsyncMultiMap;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * @author Danila Ponomarenko
 */
public final class HazelcastMultiMap<K, V> implements AsyncMultiMap<K, V> {
    private final Vertx vertx;
    private final HazelcastInstance hazelcast;
    private final String name;
    private final Serializer<K, String> keySerializer;
    private final Serializer<V, String> valueSerializer;

    public HazelcastMultiMap(@NotNull String name,
                             @NotNull Vertx vertx,
                             @NotNull HazelcastInstance hazelcast,
                             @NotNull Serializer<K, String> keySerializer,
                             @NotNull Serializer<V, String> valueSerializer) {
        this.name = Objects.requireNonNull(name, "name");
        this.vertx = Objects.requireNonNull(vertx, "vertx");
        this.hazelcast = Objects.requireNonNull(hazelcast, "hazelcast");
        this.keySerializer = Objects.requireNonNull(keySerializer, "keySerializer");
        this.valueSerializer = Objects.requireNonNull(valueSerializer, "valueSerializer");
    }

    @NotNull
    public Future<Collection<V>> get(@NotNull K key) {
        Objects.requireNonNull(key, "key");


        final Future<Collection<V>> result = Future.future();
        multimap().compose(m -> result.complete(get(key, m)), result);
        return result;
    }

    @NotNull
    public Future<Boolean> add(@NotNull K key, @NotNull V value) {
        Objects.requireNonNull(key, "key");
        final Future<Boolean> result = Future.future();
        multimap().compose(m -> result.complete(add(key, value, m)), result);
        return result;
    }

    @NotNull
    private Future<MultiMap<String, String>> multimap() {
        final Future<MultiMap<String, String>> future = Future.future();
        vertx.executeBlocking(event -> event.complete(hazelcast.getMultiMap(name)), future.completer());
        return future;
    }

    @NotNull
    private Collection<V> get(@NotNull K key, @NotNull MultiMap<String, String> map) {
        return valueSerializer.from(map.get(keySerializer.to(key)));
    }

    private boolean add(@NotNull K key, @NotNull V value, @NotNull MultiMap<String, String> map) {
        return map.put(keySerializer.to(key), valueSerializer.to(value));
    }
}
