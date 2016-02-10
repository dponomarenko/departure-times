package com.uber.departure.times.common;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public interface Publisher<T> {
    void subscribe(@NotNull Consumer<T> consumer);
}
