package com.uber.departure.times.common;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.Vertx;

/**
 * @author Danila Ponomarenko
 */
public abstract class VerticleBean<C extends VerticleConfiguration> {
    protected final Vertx vertx;
    protected final C conf;

    public VerticleBean(@NotNull Vertx vertx, @NotNull C conf) {
        this.vertx = Objects.requireNonNull(vertx, "vertx");
        this.conf = Objects.requireNonNull(conf, "conf");
    }
}
