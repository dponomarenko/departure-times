package com.uber.departure.times.pojo;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.uber.departure.times.common.JsonBased;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class StopPredictions extends JsonBased {
    public StopPredictions(@NotNull JsonObject json) {
        super(json);
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public StopPredictions(@NotNull String agency,
                           @NotNull String route,
                           @NotNull String stop,
                           @NotNull String direction,
                           @NotNull int[] predictions) {
        super();
        json.put(AGENCY, agency);
        json.put(ROUTE, route);
        json.put(STOP, stop);
        json.put(DIRECTION, direction);
        json.put(PREDICTIONS, toJsonArray(predictions));
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    private static JsonArray toJsonArray(@NotNull int[] value) {
        final JsonArray array = new JsonArray();
        for (int p : value) {
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
                && validateNotNullNotEmpty(getPredictions(json));
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

    public static final String PREDICTIONS = "prdctns";

    @Nullable
    public static int[] getPredictions(@NotNull JsonObject json) {
        final JsonArray jsonArray = json.getJsonArray(PREDICTIONS);
        final int size = jsonArray.size();
        final int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = jsonArray.getInteger(i);
        }
        return result;
    }

    public static final String DISTANCE = "dist";

    @NotNull
    private static Integer getDistance(@NotNull JsonObject json) {
        return json.getInteger(DISTANCE, 0);
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
    public int[] getPredictions() {
        //noinspection ConstantConditions
        return getPredictions(json);
    }

    public int getDistance() {
        //noinspection ConstantConditions
        return getDistance(json);
    }

    public void setDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance");
        }
        json.put(DISTANCE, distance);
    }
}
