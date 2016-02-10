package com.uber.departure.times.hub.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.clients.DataProviderClient;
import com.uber.departure.times.common.pojo.Location;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.StopPredictions;
import com.uber.departure.times.common.pojo.Stops;
import com.uber.departure.times.common.pojo.StopsPredictions;
import com.uber.departure.times.hub.client.PredictionsClient;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class PredictionsServer extends PredictionsClient {
    @Autowired
    private DataProviderClient dataProviderClient;
    @Autowired
    private NearbyStopsService nearbyStopsService;


    @PostConstruct
    protected void init() {
        super.init();
        eventBus.consumer(GET_DEPARTURES_ADDRESS, this::getDepartures);
    }

    private void getDepartures(@NotNull Message<Location> message) {
        final Future<Map<Stop, Integer>> stop2Distance = nearbyStopsService.detect(message.body());
        final Future<Map<Stop, StopPredictions>> predictions = getPredictions(toStops(stop2Distance));
        final Future<StopsPredictions> result = setDistances(stop2Distance, predictions);
        result.setHandler(r -> {
            if (r.succeeded()) {
                message.reply(r.result());
            } else {
                message.fail(1, "failed to get departures");
            }
        });
    }

    @NotNull
    private Future<Stops> toStops(@NotNull Future<Map<Stop, Integer>> stops) {
        final Future<Stops> result = Future.future();
        stops.compose(m -> result.complete(new Stops(m.keySet())), result);
        return result;
    }

    @NotNull
    private Future<StopsPredictions> setDistances(@NotNull Future<Map<Stop, Integer>> stop2Distance, @NotNull Future<Map<Stop, StopPredictions>> f) {
        final Future<StopsPredictions> result = Future.future();
        f.compose(m -> {
            final List<StopPredictions> list = new ArrayList<>();
            for (Map.Entry<Stop, StopPredictions> e : m.entrySet()) {
                final Stop key = e.getKey();
                final StopPredictions value = e.getValue();
                value.setDistance(stop2Distance.result().get(key));
                list.add(value);
            }
            result.complete(new StopsPredictions(list));
        }, result);
        return result;
    }

    @NotNull
    private Future<Map<Stop, StopPredictions>> getPredictions(@NotNull Future<Stops> stops) {
        final Future<Map<Stop, StopPredictions>> result = Future.future();
        stops.compose(s -> loadPredictions(s).compose(result::complete, result), result);
        return result;
    }

    @NotNull
    private Future<Map<Stop, StopPredictions>> loadPredictions(@NotNull Stops stops) {
        final Map<Stop, Future<StopPredictions>> futures = new HashMap<>();
        for (Stop s : stops) {
            futures.put(s, dataProviderClient.predict(s.getAgencyId(), s.getRouteId(), s.getStopId()));
        }

        //noinspection unchecked
        final CompositeFuture all = CompositeFuture.all((List) futures);
        final Future<Map<Stop, StopPredictions>> result = Future.future();
        all.compose(r -> {
            final Map<Stop, StopPredictions> predictions = new HashMap<>();
            for (Map.Entry<Stop, Future<StopPredictions>> f : futures.entrySet()) {
                predictions.put(f.getKey(), f.getValue().result());
            }
            result.complete(predictions);
        }, result);
        return result;
    }
}
