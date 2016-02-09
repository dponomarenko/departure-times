package com.uber.departure.times.pojo;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public final class Cells extends TypedCollection<Cell> {
    public Cells(@NotNull Collection<Cell> cells) {
        super(cells);
    }
}