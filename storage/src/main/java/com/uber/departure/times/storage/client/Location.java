package com.uber.departure.times.storage.client;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class Location implements Serializable {
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        if (latitude <= -90 || latitude >= 90) {
            throw new IllegalArgumentException("wrong latitude " + latitude);
        }
        this.latitude = latitude;
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("wrong longitude " + longitude);
        }
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static int distance(@NotNull Location l1, @NotNull Location l2) {
        return (int) distanceInRadians(l1, l2) * EARTH_MEAN_RADIUS_METERS;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Location other = (Location) obj;
        return new EqualsBuilder()
                .append(latitude, other.latitude)
                .append(longitude, other.longitude)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(37, 31)
                .append(latitude)
                .append(longitude)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .toString();
    }
}
