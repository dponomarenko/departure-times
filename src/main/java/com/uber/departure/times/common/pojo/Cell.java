package com.uber.departure.times.common.pojo;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.uber.departure.times.common.JsonBased;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class Cell extends JsonBased {
    public static final int GRANULARITY = 1000;
    public static final double GRANULARITY_MINUS_ONE = 1 / (double) GRANULARITY;
    public static final int MAX_X = 180 * GRANULARITY;
    public static final int MAX_Y = 90 * GRANULARITY;

    public static Cell from(@NotNull Location location) {
        return new Cell((int) location.getLongitude() * GRANULARITY, (int) location.getLatitude() * GRANULARITY);
    }

    public Cell(int x, int y) {
        super();
        json.put(X, normalizeX(x));
        json.put(Y, y);
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public Cell(@NotNull JsonObject json) {
        super(json);
        if (!validate(json)) {
            throw new IllegalArgumentException("wrong json");
        }
    }

    public static boolean validate(@NotNull JsonObject json) {
        Objects.requireNonNull(json, "json");
        final Integer x = getX(json);
        if (x == null || !validateX(x)) {
            return false;
        }
        final Integer y = getY(json);
        //noinspection RedundantIfStatement
        if (y == null || !validateY(y)) {
            return false;
        }
        return true;
    }

    @NotNull
    public Location location() {
        return new Location(getY() * GRANULARITY_MINUS_ONE, getX() * GRANULARITY_MINUS_ONE);
    }

    private int normalizeX(int x) {
        final int result = (x + MAX_X) % (2 * MAX_X);
        return result > 0 ? result - MAX_X : result + MAX_X;
    }

    private static boolean validateX(int x) {
        return -MAX_X <= x && x < MAX_X;
    }

    private static boolean validateY(int y) {
        return -MAX_Y < y && y < MAX_Y;
    }

    public static final String X = "x";
    public static final String Y = "y";

    @Nullable
    private static Integer getX(@NotNull JsonObject json) {
        return json.getInteger(X);
    }

    @Nullable
    public static Integer getY(@NotNull JsonObject json) {
        return json.getInteger(Y);
    }

    public int getX() {
        //noinspection ConstantConditions
        return getX(json);
    }

    public int getY() {
        //noinspection ConstantConditions
        return getY(json);
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
        public Cell apply(@NotNull Cell coordinates) {
            int x = coordinates.getX() + deltaX;
            int y = coordinates.getY() + deltaY;
            if (!validateY(y)) {
                return null;
            }

            return new Cell(x, y);
        }
    }
}
