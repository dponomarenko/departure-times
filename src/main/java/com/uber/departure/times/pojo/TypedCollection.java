package com.uber.departure.times.pojo;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.JsonConvertible;

import io.vertx.core.json.JsonArray;

/**
 * @author Danila Ponomarenko
 */
public abstract class TypedCollection<T extends JsonConvertible> implements Iterable<T> {
    private final Collection<T> values;

    public TypedCollection(@NotNull Collection<T> values) {
        this.values = Objects.requireNonNull(values, "values");
    }

    @NotNull
    public Collection<T> getValues() {
        return values;
    }

    @NotNull
    public JsonArray toJsonArray() {
        return JsonConvertible.toJsonArray(values, JsonConvertible::toJson);
    }

    public int size() {
        return values.size();
    }

    @Override
    public Iterator<T> iterator() {
        return values.iterator();
    }
}