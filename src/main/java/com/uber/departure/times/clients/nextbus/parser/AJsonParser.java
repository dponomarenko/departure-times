package com.uber.departure.times.clients.nextbus.parser;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public abstract class AJsonParser<T> implements Function<Buffer, Collection<T>> {
    private final Function<JsonObject, JsonArray> getter;
    private final Supplier<Collection<T>> ctor;

    protected AJsonParser(@NotNull Function<JsonObject, JsonArray> getter, @NotNull Supplier<Collection<T>> ctor) {
        this.getter = Objects.requireNonNull(getter, "getter");
        this.ctor = Objects.requireNonNull(ctor, "ctor");
    }

    @Override
    public final Collection<T> apply(Buffer buffer) {
        final Collection<T> result = ctor.get();
        final JsonObject json = buffer.toJsonObject();
        for (Object o : getter.apply(json)) {
            result.add(parse(o));
        }
        return result;
    }

    @NotNull
    protected abstract T parse(@NotNull Object o);
}
