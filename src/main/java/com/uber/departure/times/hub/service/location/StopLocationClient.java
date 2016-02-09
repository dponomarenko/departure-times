package com.uber.departure.times.hub.service.location;

import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.pojo.Cell;
import com.uber.departure.times.pojo.Cells;
import com.uber.departure.times.pojo.Stop;
import com.uber.departure.times.pojo.Stops;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

/**
 * @author Danila Ponomarenko
 */
public final class StopLocationClient {
    private EventBus eventBus;

    public StopLocationClient(@NotNull EventBus eventBus) {
        this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
        StopLocationsService.initCodecs(eventBus);
    }

    @NotNull
    public Future<Stops> get(@NotNull Cell key) {
        final Future<Stops> result = Future.future();
        eventBus.send(StopLocationsService.GET_ADDRESS, key, r -> {
            if (r.succeeded()) {
                result.complete((Stops) r.result().body());
            } else {
                result.fail(r.cause());
            }
        });
        return result;
    }

    @NotNull
    public Future<Stops> getMany(@NotNull Set<Cell> cells) {
        final Future<Stops> result = Future.future();
        eventBus.send(StopLocationsService.GET_MANY_ADDRESS, new Cells(cells), r -> {
            if (r.succeeded()) {
                result.complete((Stops) r.result().body());
            } else {
                result.fail(r.cause());
            }
        });
        return result;
    }

    @NotNull
    public Future<Boolean> add(@NotNull Stop stop) {
        final Future<Boolean> result = Future.future();
        eventBus.send(StopLocationsService.ADD_ADDRESS, stop, r -> {
            if (r.succeeded()) {
                result.complete((Boolean) r.result().body());
            } else {
                result.fail(r.cause());
            }
        });
        return result;
    }
}
