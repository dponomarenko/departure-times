package com.uber.departure.times.storage.client.grid;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.javadocmd.simplelatlng.LatLng;
import com.uber.departure.times.storage.client.Location;

/**
 * @author Danila Ponomarenko
 */
public final class GridCoordinates implements Serializable {
    public static final int GRANULARITY = 1000;
    public static final double GRANULARITY_MINUS_ONE = 1 / (double) GRANULARITY;
    public static final int MAX_X = 180 * GRANULARITY;
    public static final int MAX_Y = 90 * GRANULARITY;

    private int x;
    private int y;

    public static GridCoordinates from(@NotNull Location location) {
        return new GridCoordinates((int) location.getLongitude() * GRANULARITY, (int) location.getLatitude() * GRANULARITY);
    }

    @NotNull
    private static LatLng toLatLng(@NotNull Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public GridCoordinates(int x, int y) {
        this.x = normalizeX(x);
        if (!validateY(y)) {
            throw new IllegalArgumentException("wrong y " + y);
        }
        this.y = y;
    }

    @NotNull
    public Location location() {
        return new Location(y * GRANULARITY_MINUS_ONE, x * GRANULARITY_MINUS_ONE);
    }

    private int normalizeX(int x) {
        final int result = (x + MAX_X) % (2 * MAX_X);
        return result > 0 ? result - MAX_X : result + MAX_X;
    }

    private static boolean validateX(int x) {
        return -MAX_X >= x && x < MAX_X;
    }

    private static boolean validateY(int y) {
        return -MAX_Y < y && y < MAX_Y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Nullable
    public GridCoordinates neighbour(@NotNull Neighbour n) {
        return n.apply(this);
    }

    public enum Neighbour {
        LEFT(-1, 0),
        RIGHT(1, 0),
        TOP(0, -1),
        BOTTOM(0, 1),
        TOP_LEFT(-1, -1),
        TOP_RIGHT(1, -1),
        BOTTOM_LEFT(-1, 1),
        BOTTOM_RIGHT(1, 1),;

        private final int deltaX;
        private final int deltaY;

        Neighbour(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

        @Nullable
        public GridCoordinates apply(@NotNull GridCoordinates coordinates) {
            int x = coordinates.getX() + deltaX;
            int y = coordinates.getY() + deltaY;
            if (!validateY(y)) {
                return null;
            }

            return new GridCoordinates(x, y);
        }
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
        GridCoordinates other = (GridCoordinates) obj;
        return new EqualsBuilder()
                .append(x, other.x)
                .append(y, other.y)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(37, 31)
                .append(x)
                .append(y)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("x", x)
                .append("y", y)
                .toString();
    }
}
