package com.uber.departure.times.common.pojo;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.uber.departure.times.common.JsonBased;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class StopId extends JsonBased {
    public StopId(@NotNull String agencyId,
                  @NotNull String routeId,
                  @NotNull String stopId) {
        super();
        json.put(AGENCY_ID, agencyId);
        json.put(ROUTE_ID, routeId);
        json.put(STOP_ID, stopId);
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public StopId(@NotNull JsonObject json) {
        super(json);
        //noinspection ConstantConditions
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public static boolean validate(@NotNull JsonObject json) {
        Objects.requireNonNull(json, "json");
        //noinspection ConstantConditions
        return validateNotNull(getAgencyId(json))
                && validateNotNull(getRouteId(json))
                && validateNotNull(getStopId(json));
    }

    public static final String AGENCY_ID = "agc";

    @Nullable
    public static String getAgencyId(@NotNull JsonObject json) {
        return json.getString(AGENCY_ID);
    }

    public static final String ROUTE_ID = "rt";

    @Nullable
    public static String getRouteId(@NotNull JsonObject json) {
        return json.getString(ROUTE_ID);
    }

    public static final String STOP_ID = "stop";

    @Nullable
    public static String getStopId(@NotNull JsonObject json) {
        return json.getString(STOP_ID);
    }

    @NotNull
    public String getAgencyId() {
        //noinspection ConstantConditions
        return getAgencyId(json);
    }


    @NotNull
    public String getRouteId() {
        //noinspection ConstantConditions
        return getRouteId(json);
    }


    @NotNull
    public String getStopId() {
        //noinspection ConstantConditions
        return getStopId(json);
    }
}
