package com.uber.departure.times.pojo;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.uber.departure.times.common.JsonBased;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class Location extends JsonBased {
    public Location(@NotNull JsonObject json) {
        super(json);
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public Location(double latitude, double longitude) {
        super();
        json.put(LATITUDE, latitude);
        json.put(LONGITUDE, longitude);
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong arguments");
        }
    }

    public static boolean validate(@NotNull JsonObject json) {
        Objects.requireNonNull(json, "json");
        final Double latitude = getLatitude(json);
        if (latitude == null || !validateLatitude(latitude)) {
            return false;
        }
        final Double longitude = getLongitude(json);
        //noinspection RedundantIfStatement
        if (longitude == null || !validateLongitude(longitude)) {
            return false;
        }
        return true;
    }

    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";

    public static boolean validateLatitude(double latitude) {
        return -90 < latitude && latitude < 90;
    }

    public static boolean validateLongitude(double longitude) {
        return -180 <= longitude && longitude <= 180;
    }

    @Nullable
    private static Double getLatitude(@NotNull JsonObject json) {
        return json.getDouble(LATITUDE);
    }

    @Nullable
    public static Double getLongitude(@NotNull JsonObject json) {
        return json.getDouble(LONGITUDE);
    }

    public double getLatitude() {
        //noinspection ConstantConditions
        return getLatitude(json);
    }

    public double getLongitude() {
        //noinspection ConstantConditions
        return getLongitude(json);
    }

    public static int distance(@NotNull Location l1, @NotNull Location l2) {
        return (int) (distanceInRadians(l1, l2) * EARTH_MEAN_RADIUS_METERS);
    }

    public static final int EARTH_MEAN_RADIUS_METERS = 6371009;

    private static double distanceInRadians(Location point1, Location point2) {
        double lat1R = Math.toRadians(point1.getLatitude());
        double lat2R = Math.toRadians(point2.getLatitude());
        double dLatR = Math.abs(lat2R - lat1R);
        double dLngR = Math.abs(Math.toRadians(point2.getLongitude() - point1.getLongitude()));
        double a = Math.sin(dLatR / 2) * Math.sin(dLatR / 2) + Math.cos(lat1R) * Math.cos(lat2R)
                * Math.sin(dLngR / 2) * Math.sin(dLngR / 2);
        return 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
