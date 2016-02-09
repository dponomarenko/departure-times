package com.uber.departure.times.hub.service.prediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.clients.ProviderClient;
import com.uber.departure.times.common.EventBusListener;
import com.uber.departure.times.common.VerticleBean;
import com.uber.departure.times.hub.HubConfiguration;
import com.uber.departure.times.hub.service.location.StopLocationClient;
import com.uber.departure.times.pojo.Cell;
import com.uber.departure.times.pojo.Location;
import com.uber.departure.times.pojo.Stop;
import com.uber.departure.times.pojo.StopPredictions;
import com.uber.departure.times.pojo.Stops;
import com.uber.departure.times.pojo.StopsPredictions;
import com.uber.departure.times.pojo.codec.LocationMessageCodec;
import com.uber.departure.times.pojo.codec.StopPredictionsMessageCodec;
import com.uber.departure.times.pojo.codec.StopsPredictionsMessageCodec;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

/**
 * @author Danila Ponomarenko
 */
public final class PredictionsService extends VerticleBean<HubConfiguration> implements EventBusListener {
    protected static final String GET_DEPARTURES_ADDRESS = "hub.departures";

    private final ProviderClient providerClient;
    private final StopLocationClient stopLocationClient;

    public PredictionsService(@NotNull Vertx vertx,
                              @NotNull StopLocationClient stopLocationClient, @NotNull ProviderClient providerClient,
                              @NotNull HubConfiguration conf) {
        super(vertx, conf);
        this.stopLocationClient = Objects.requireNonNull(stopLocationClient, "stopLocationClient");
        this.providerClient = Objects.requireNonNull(providerClient, "providerClient");
    }

    @Override
    public void listen(@NotNull EventBus eventBus) {
        initCodecs(eventBus);
        vertx.eventBus().consumer(PredictionsService.GET_DEPARTURES_ADDRESS, this::getDepartures);
    }

    protected static void initCodecs(@NotNull EventBus eventBus) {
        eventBus.registerCodec(new StopsPredictionsMessageCodec());
        eventBus.registerCodec(new StopPredictionsMessageCodec());
        eventBus.registerCodec(new LocationMessageCodec());
    }


    private void getDepartures(@NotNull Message<Location> message) {
        final Location location = message.body();
        final Future<Stops> stops = getNearbyStops(location);

        final Future<Map<Stop, StopPredictions>> predictions = getPredictions(stops);
        final Future<StopsPredictions> result = setDistances(location, predictions);
        result.setHandler(r -> {
            if (r.succeeded()) {
                message.reply(r.result());
            } else {
                message.fail(1, "failed to get departures");
            }
        });
    }

    @NotNull
    private Future<StopsPredictions> setDistances(@NotNull Location location, @NotNull Future<Map<Stop, StopPredictions>> f) {
        final Future<StopsPredictions> result = Future.future();
        f.compose(m -> {
            final List<StopPredictions> list = new ArrayList<>();
            for (Map.Entry<Stop, StopPredictions> e : m.entrySet()) {
                final Stop key = e.getKey();
                final StopPredictions value = e.getValue();
                value.setDistance(Location.distance(key.getLocation(), location));
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
            futures.put(s, providerClient.predict(s.getAgencyId(), s.getRouteId(), s.getStopId()));
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

    @NotNull
    private Future<Stops> getNearbyStops(@NotNull Location location) {
        final Cell cell = Cell.from(location);
        final Set<Cell> neighbours = neighbourCells(cell);

        final Future<Stops> nearbyCellsStops = stopLocationClient.getMany(neighbours);
        final Future<Stops> result = Future.future();
        nearbyCellsStops.compose(s -> result.complete(filterNearbyStops(location, s)), result);
        return result;
    }

    @NotNull
    private Stops filterNearbyStops(@NotNull Location location, @NotNull Stops stops) {
        final List<Stop> result = new ArrayList<>();
        for (Stop s : stops) {
            if (Location.distance(location, s.getLocation()) < conf.getSearchRadius()) {
                result.add(s);
            }
        }
        return new Stops(result);
    }

    @NotNull
    private Set<Cell> neighbourCells(@NotNull Cell location) {
        final Stream<Cell> result = Arrays.stream(Cell.Neighbour.values())
                .map(n -> n.apply(location))
                .filter(g -> g != null);
        return Stream.concat(Stream.of(location), result).collect(Collectors.toSet());
    }
}
