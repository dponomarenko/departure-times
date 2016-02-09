package com.uber.departure.times.pojo;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.uber.departure.times.common.JsonBased;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class Route extends JsonBased {
    public Route(@NotNull JsonObject json) {
        super(json);
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public Route(@NotNull String agencyTag, @NotNull String routeTag) {
        super();
        json.put(AGENCY_TAG, agencyTag);
        json.put(ROUTE_TAG, routeTag);
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong arguments");
        }
    }

    public static boolean validate(@NotNull JsonObject json) {
        Objects.requireNonNull(json, "json");
        return validateNotNullNotEmpty(getRouteTag(json)) && validateNotNullNotEmpty(getAgencyTag(json));
    }

    public static final String ROUTE_TAG = "route-tag";
    public static final String AGENCY_TAG = "agency-tag";

    @Nullable
    public static String getRouteTag(@NotNull JsonObject json) {
        return json.getString(ROUTE_TAG);
    }

    @Nullable
    public static String getAgencyTag(@NotNull JsonObject json) {
        return json.getString(AGENCY_TAG);
    }

    @NotNull
    public String getRouteTag() {
        //noinspection ConstantConditions
        return getRouteTag(json);
    }

    @NotNull
    public String getAgencyTag() {
        //noinspection ConstantConditions
        return getAgencyTag(json);
    }
}
