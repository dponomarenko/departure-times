package com.uber.departure.times.common;

import org.jetbrains.annotations.NotNull;

/**
 * @author Danila Ponomarenko
 */
public interface Listener<T> {
    void listen(@NotNull T t);
}
