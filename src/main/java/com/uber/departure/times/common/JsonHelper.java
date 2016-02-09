package com.uber.departure.times.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.json.JsonArray;

/**
 * @author Danila Ponomarenko
 */
public final class JsonHelper {
    @NotNull
    public static JsonArray toArray(@NotNull Collection<String> collection,
                                    @NotNull Function<String, Object> converter) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(converter, "converter");
        final JsonArray result = new JsonArray();
        collection.stream().map(converter).forEach(result::add);
        return result;
    }

    @NotNull
    public static Set<String> toSetOfStrings(@NotNull JsonArray array) {
        Objects.requireNonNull(array, "array");
        final Set<String> result = new HashSet<>();
        for (Object o : array) {
            result.add((String) o);
        }
        return result;
    }

    private JsonHelper() {
    }
}
