package com.uber.departure.times.common.pojo;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class Stops extends TypedCollection<Stop> {
    public Stops(@NotNull Collection<Stop> stops) {
        super(stops);
    }
}
