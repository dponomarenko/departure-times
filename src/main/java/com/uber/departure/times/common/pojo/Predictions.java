package com.uber.departure.times.common.pojo;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class Predictions extends TypedCollection<Prediction> {
    public Predictions(@NotNull Collection<Prediction> p) {
        super(p);
    }
}
