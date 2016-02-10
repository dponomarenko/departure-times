package com.uber.departure.times.hub.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.pojo.Cell;
import com.uber.departure.times.common.pojo.Location;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.Stops;
import com.uber.departure.times.hub.client.StopLocationClient;

import io.vertx.core.Future;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class NearbyStopsService {
    @Autowired
    private StopLocationClient stopLocationClient;
    @Autowired
    private HubConfiguration conf;

    @NotNull
    public Future<Map<Stop, Integer>> detect(@NotNull Location location) {
        final Cell cell = Cell.from(location);
        final Set<Cell> neighbours = neighbourCells(cell);

        final Future<Stops> nearbyCellsStops = stopLocationClient.getMany(neighbours);
        final Future<Map<Stop, Integer>> result = Future.future();
        nearbyCellsStops.compose(s -> result.complete(filterNearbyStops(location, s)), result);
        return result;
    }

    @NotNull
    private Map<Stop, Integer> filterNearbyStops(@NotNull Location location, @NotNull Stops stops) {
        final Map<Stop, Integer> result = new HashMap<>();
        for (Stop s : stops) {
            final int distance = Location.distance(location, s.getLocation());
            if (distance < conf.getSearchRadius()) {
                result.put(s, distance);
            }
        }
        return result;
    }

    @NotNull
    private Set<Cell> neighbourCells(@NotNull Cell location) {
        final Stream<Cell> result = Arrays.stream(Cell.Neighbour.values())
                .map(n -> n.apply(location))
                .filter(g -> g != null);
        return Stream.concat(Stream.of(location), result).collect(Collectors.toSet());
    }
}
