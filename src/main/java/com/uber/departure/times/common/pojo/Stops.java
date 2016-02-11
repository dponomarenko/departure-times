package com.uber.departure.times.common.pojo;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class Stops extends TypedCollection<Stop> {
    public Stops(@NotNull Collection<Stop> stops) {
        super(stops);
    }

    public static Stops empty() {
        return new Stops(Collections.emptyList());
    }
}
