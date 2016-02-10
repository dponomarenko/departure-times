package com.uber.departure.times.common.pojo;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.uber.departure.times.common.JsonBased;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public class Stop extends JsonBased {
    public Stop(@NotNull StopId id,
                @NotNull Location location) {
        super();
        json.put(LOCATION, Objects.requireNonNull(location, "location").toJson());
        json.put(STOP_ID, id.toJson());
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public Stop(@NotNull JsonObject json) {
        super(json);
        //noinspection ConstantConditions
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public static final String LOCATION = "loc";

    @Nullable
    public static JsonObject getLocation(@NotNull JsonObject json) {
        return json.getJsonObject(LOCATION);
    }

    public static boolean validate(@NotNull JsonObject json) {
        Objects.requireNonNull(json, "json");
        //noinspection ConstantConditions
        return StopId.validate(getStopId(json)) && Location.validate(getLocation(json));
    }

    public static final String STOP_ID = "id";

    @Nullable
    public static JsonObject getStopId(@NotNull JsonObject json) {
        return json.getJsonObject(STOP_ID);
    }

    @NotNull
    public Location getLocation() {
        //noinspection ConstantConditions
        return new Location(getLocation(json));
    }

    @NotNull
    public StopId getStopId() {
        //noinspection ConstantConditions
        return new StopId(getStopId(json));
    }
}
