package com.uber.departure.times.hub.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.pojo.Location;
import com.uber.departure.times.common.pojo.Prediction;
import com.uber.departure.times.common.pojo.Predictions;
import com.uber.departure.times.common.pojo.ProvidedPredictions;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.StopId;
import com.uber.departure.times.hub.client.PredictionsClient;

import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class PredictionsServer extends PredictionsClient {
    private static final Logger logger = LoggerFactory.getLogger(PredictionsServer.class);

    @Autowired
    private NearbyStopsService nearbyStopsService;
    @Autowired
    private PredictionsLoaderService loaderService;


    @PostConstruct
    protected void init() {
        super.init();
        eventBus.consumer(GET_DEPARTURES_ADDRESS, this::getDepartures);
    }

    private void getDepartures(@NotNull Message<Location> message) {
        final Location location = message.body();
        final Future<Map<StopId, Pair<Stop, Integer>>> stop2Distance = nearbyStopsService.detect(location);
        final Future<Map<StopId, ProvidedPredictions>> predictions = load(getIds(stop2Distance));
        final Future<Predictions> result = setDistances(stop2Distance, predictions);
        result.setHandler(r -> {
            if (r.succeeded()) {
                message.reply(r.result());
            } else {
                logger.error("getDepartures for " + location, r.cause());
                message.fail(1, "failed to get departures");
            }
        });
    }


    @NotNull
    public Future<Map<StopId, ProvidedPredictions>> load(@NotNull Future<Set<StopId>> stops) {
        final Future<Map<StopId, ProvidedPredictions>> result = Future.future();
        stops.compose(s -> loaderService.load(s).compose(result::complete, result), result);
        return result;
    }

    @NotNull
    private Future<Set<StopId>> getIds(@NotNull Future<Map<StopId, Pair<Stop, Integer>>> stops) {
        final Future<Set<StopId>> result = Future.future();
        stops.compose(m -> result.complete(m.keySet()), result);
        return result;
    }

    @NotNull
    private Future<Predictions> setDistances(@NotNull Future<Map<StopId, Pair<Stop, Integer>>> stop2Distance,
                                             @NotNull Future<Map<StopId, ProvidedPredictions>> f) {
        final Future<Predictions> result = Future.future();
        f.compose(m -> {
            final List<Prediction> list = new ArrayList<>();
            for (Map.Entry<StopId, ProvidedPredictions> e : m.entrySet()) {
                final StopId key = e.getKey();
                final ProvidedPredictions value = e.getValue();
                if (value != null) {
                    list.add(new Prediction(
                            value.getAgency(),
                            value.getRoute(),
                            value.getStop(),
                            value.getDirection(),
                            stop2Distance.result().get(key).getValue(),
                            predict(value.getEpoch())
                    ));
                }
            }
            result.complete(new Predictions(list));
        }, result);
        return result;
    }

    @NotNull
    private int[] predict(@NotNull long[] epoch) {
        final int size = epoch.length;
        final int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = (int) TimeUnit.MILLISECONDS.toMinutes(epoch[i] - System.currentTimeMillis());
        }
        return result;
    }
}
