package com.uber.departure.times.pojo;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class StopsPredictions extends TypedCollection<StopPredictions> {
    public StopsPredictions(@NotNull Collection<StopPredictions> stops) {
        super(stops);
    }
}
