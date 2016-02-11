package com.uber.departure.times.common;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

/**
 * @author Danila Ponomarenko
 */
public final class SpringVerticleConnector {
    private final List<Future> start = new ArrayList<>();
    private final List<Future> stop = new ArrayList<>();

    public void registerStartFuture(@NotNull Future f) {
        start.add(f);
    }

    public void registerStopFuture(@NotNull Future f) {
        stop.add(f);
    }

    @NotNull
    public Future<CompositeFuture> startFuture() {
        if (start.isEmpty()){
            return Future.succeededFuture();
        }
        return CompositeFuture.all(start);
    }

    @NotNull
    public Future<CompositeFuture> stopFuture() {
        if (stop.isEmpty()){
            return Future.succeededFuture();
        }
        return CompositeFuture.all(stop);
    }
}
