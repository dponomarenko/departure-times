package com.uber.departure.times.common.pojo;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.uber.departure.times.common.JsonBased;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class ProvidedPredictions extends JsonBased {
    public ProvidedPredictions(@NotNull JsonObject json) {
        super(json);
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public ProvidedPredictions(@NotNull String agency,
                               @NotNull String route,
                               @NotNull String stop,
                               @NotNull String direction,
                               @NotNull long[] epoch) {
        super();
        json.put(AGENCY, agency);
        json.put(ROUTE, route);
        json.put(STOP, stop);
        json.put(DIRECTION, direction);
        json.put(EPOCH, toJsonArray(epoch));
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    private static JsonArray toJsonArray(@NotNull long[] value) {
        final JsonArray array = new JsonArray();
        for (long p : value) {
            array.add(p);
        }
        return array;
    }

    public static boolean validate(@NotNull JsonObject json) {
        Objects.requireNonNull(json, "json");
        return validateNotNullNotEmpty(getAgency(json))
                && validateNotNullNotEmpty(getRoute(json))
                && validateNotNullNotEmpty(getStop(json))
                && validateNotNullNotEmpty(getDirection(json))
                && validateNotNullNotEmpty(getEpoch(json));
    }

    public static final String AGENCY = "agc";

    @Nullable
    public static String getAgency(@NotNull JsonObject json) {
        return json.getString(AGENCY);
    }

    public static final String ROUTE = "rt";

    @Nullable
    public static String getRoute(@NotNull JsonObject json) {
        return json.getString(ROUTE);
    }

    public static final String STOP = "stop";

    @Nullable
    public static String getStop(@NotNull JsonObject json) {
        return json.getString(STOP);
    }

    public static final String DIRECTION = "dir";

    @Nullable
    public static String getDirection(@NotNull JsonObject json) {
        return json.getString(DIRECTION);
    }

    public static final String EPOCH = "epoch";

    @Nullable
    public static long[] getEpoch(@NotNull JsonObject json) {
        return toLongArray(json.getJsonArray(EPOCH));
    }

    @NotNull
    public String getAgency() {
        //noinspection ConstantConditions
        return getAgency(json);
    }

    @NotNull
    public String getRoute() {
        //noinspection ConstantConditions
        return getRoute(json);
    }

    @NotNull
    public String getStop() {
        //noinspection ConstantConditions
        return getStop(json);
    }

    @NotNull
    public String getDirection() {
        //noinspection ConstantConditions
        return getDirection(json);
    }

    @NotNull
    public long[] getEpoch() {
        //noinspection ConstantConditions
        return getEpoch(json);
    }
}
