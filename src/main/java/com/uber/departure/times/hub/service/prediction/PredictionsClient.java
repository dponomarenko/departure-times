package com.uber.departure.times.hub.service.prediction;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.pojo.Location;
import com.uber.departure.times.pojo.StopsPredictions;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

/**
 * @author Danila Ponomarenko
 */
public final class PredictionsClient {
    private EventBus eventBus;

    public PredictionsClient(@NotNull EventBus eventBus) {
        this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
        PredictionsService.initCodecs(eventBus);
    }

    @NotNull
    public Future<StopsPredictions> get(@NotNull Location location) {
        final Future<StopsPredictions> result = Future.future();
        eventBus.send(PredictionsService.GET_DEPARTURES_ADDRESS, location, r -> {
            if (r.succeeded()) {
                result.complete((StopsPredictions) r.result().body());
            } else {
                result.fail(r.cause());
            }
        });
        return result;
    }
}
