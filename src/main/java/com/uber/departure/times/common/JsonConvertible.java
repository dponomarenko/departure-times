package com.uber.departure.times.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public interface JsonConvertible {
    @NotNull
    JsonObject toJson();

    @NotNull
    static JsonArray toJsonArray(@NotNull Collection<String> collection) {
        Objects.requireNonNull(collection, "collection");
        final JsonArray result = new JsonArray();
        collection.forEach(result::add);
        return result;
    }

    @NotNull
    static <T> JsonArray toJsonArray(@NotNull Collection<T> collection, @NotNull Function<T, ?> converter) {
        Objects.requireNonNull(collection, "collection");
        final JsonArray result = new JsonArray();
        collection.forEach(e -> result.add(converter.apply(e)));
        return result;
    }

    @NotNull
    static <T> Collection<T> fromJsonArray(@NotNull JsonArray array, @NotNull Function<Object, T> converter) {
        Objects.requireNonNull(array, "array");
        Objects.requireNonNull(converter, "converter");
        final Collection<T> result = new ArrayList<>();
        array.forEach(o -> result.add(converter.apply(o)));
        return result;
    }
}
