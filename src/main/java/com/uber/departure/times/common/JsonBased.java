package com.uber.departure.times.common;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public abstract class JsonBased implements JsonConvertible {
    protected final JsonObject json;

    public JsonBased(@NotNull JsonObject json) {
        this.json = Objects.requireNonNull(json, "json");
    }

    public JsonBased() {
        this(new JsonObject());
    }

    protected static boolean validateNotNullNotEmpty(@Nullable String value) {
        return value != null && !value.isEmpty();
    }

    protected static boolean validateNotNullNotEmpty(@Nullable int[] value) {
        return value != null && value.length != 0;
    }

    protected static boolean validateNotNullNotEmpty(@Nullable long[] value) {
        return value != null && value.length != 0;
    }

    protected static boolean validateNotNullNonNegative(@Nullable Integer value) {
        return value != null && value >= 0;
    }

    @Nullable
    protected static int[] toIntArray(@NotNull JsonArray jsonArray) {
        final int size = jsonArray.size();
        final int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = jsonArray.getInteger(i);
        }
        return result;
    }

    @Nullable
    protected static long[] toLongArray(@NotNull JsonArray jsonArray) {
        final int size = jsonArray.size();
        final long[] result = new long[size];
        for (int i = 0; i < size; i++) {
            result[i] = jsonArray.getInteger(i);
        }
        return result;
    }

    @Override
    @NotNull
    public final JsonObject toJson() {
        return json;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        JsonBased other = (JsonBased) obj;
        return new EqualsBuilder()
                .append(json, other.json)
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return json.hashCode();
    }

    @Override
    public final String toString() {
        return json.encode();
    }
}
