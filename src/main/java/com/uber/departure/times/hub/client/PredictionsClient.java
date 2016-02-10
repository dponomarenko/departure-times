package com.uber.departure.times.hub.client;

import javax.annotation.PostConstruct;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.pojo.Location;
import com.uber.departure.times.common.pojo.StopsPredictions;
import com.uber.departure.times.common.pojo.codec.LocationMessageCodec;
import com.uber.departure.times.common.pojo.codec.StopPredictionsMessageCodec;
import com.uber.departure.times.common.pojo.codec.StopsPredictionsMessageCodec;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

/**
 * @author Danila Ponomarenko
 */
@Component
public class PredictionsClient {
    protected static final String GET_DEPARTURES_ADDRESS = "hub.departures";

    @Autowired
    protected EventBus eventBus;

    @PostConstruct
    protected void init() {
        eventBus.registerCodec(new StopsPredictionsMessageCodec());
        eventBus.registerCodec(new StopPredictionsMessageCodec());
        eventBus.registerCodec(new LocationMessageCodec());
    }

    @NotNull
    public Future<StopsPredictions> get(@NotNull Location location) {
        final Future<StopsPredictions> result = Future.future();
        eventBus.send(GET_DEPARTURES_ADDRESS, location, r -> {
            if (r.succeeded()) {
                result.complete((StopsPredictions) r.result().body());
            } else {
                result.fail(r.cause());
            }
        });
        return result;
    }
}
