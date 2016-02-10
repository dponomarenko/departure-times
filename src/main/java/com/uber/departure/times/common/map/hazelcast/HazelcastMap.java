package com.uber.departure.times.common.map.hazelcast;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.hazelcast.core.IMap;
import com.uber.departure.times.common.map.AsyncMap;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * @author Danila Ponomarenko
 */
public final class HazelcastMap<K, KM, V, VM> implements AsyncMap<K, V> {
    private final Vertx vertx;
    private final IMap<KM, VM> map;
    private final Serializer<K, KM> keySerializer;
    private final Serializer<V, VM> valueSerializer;
    private final long lockTimeMs;

    public HazelcastMap(@NotNull Vertx vertx,
                        @NotNull IMap<KM, VM> map,
                        @NotNull Serializer<K, KM> keySerializer,
                        @NotNull Serializer<V, VM> valueSerializer,
                        long lockTimeMs) {
        this.lockTimeMs = lockTimeMs;
        this.map = Objects.requireNonNull(map, "map");
        this.vertx = Objects.requireNonNull(vertx, "vertx");
        this.keySerializer = Objects.requireNonNull(keySerializer, "keySerializer");
        this.valueSerializer = Objects.requireNonNull(valueSerializer, "valueSerializer");
    }

    @NotNull
    @Override
    public Future<V> get(@NotNull K key) {
        Objects.requireNonNull(key, "key");
        final Future<V> result = Future.future();
        vertx.executeBlocking(event -> result.complete(get(key, map)), result.completer());
        return result;
    }

    @NotNull
    @Override
    public Future<V> put(@NotNull K key, @Nullable V value, long ttlMs) {
        Objects.requireNonNull(key, "key");
        final Future<V> result = Future.future();
        vertx.executeBlocking(event -> result.complete(put(key, value, map, ttlMs)), result.completer());
        return result;
    }

    @NotNull
    @Override
    public Future<V> computeIfAbsent(@NotNull K key, @NotNull Function<K, Future<V>> f, long ttlMs) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(f, "f");
        final Future<V> result = Future.future();
        vertx.executeBlocking(event -> computeIfAbsent(key, f, map, ttlMs).compose(result::complete, result), result.completer());
        return result;
    }

    private V get(@NotNull K key, @NotNull IMap<KM, VM> map) {
        final VM vm = map.get(keySerializer.to(key));
        if (vm == null) {
            return null;
        }
        return valueSerializer.from(vm);
    }

    private V put(@NotNull K key, @Nullable V value, @NotNull IMap<KM, VM> map, long ttlMs) {
        return valueSerializer.from(map.put(keySerializer.to(key), valueSerializer.to(value), ttlMs, TimeUnit.MILLISECONDS));
    }

    private Future<V> computeIfAbsent(@NotNull K key, @NotNull Function<K, Future<V>> f, @NotNull IMap<KM, VM> map, long ttlMs) {
        final KM mKey = keySerializer.to(key);

        map.lock(mKey, lockTimeMs, TimeUnit.MILLISECONDS);
        final Future<V> result = Future.future();
        final V value = get(key, map);
        if (value != null) {
            map.unlock(mKey);
            result.complete(value);
        } else {
            f.apply(key).compose(v -> {
                put(key, v, ttlMs);
                map.unlock(mKey);
                result.complete(v);
            }, result);
        }

        return result;
    }
}
