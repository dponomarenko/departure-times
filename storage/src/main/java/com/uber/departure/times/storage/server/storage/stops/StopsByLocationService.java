package com.uber.departure.times.storage.server.storage.stops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.uber.departure.times.storage.client.Location;
import com.uber.departure.times.storage.client.Stop;
import com.uber.departure.times.storage.client.grid.GridCoordinates;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class StopsByLocationService {

    @Autowired
    private StopsByLocationConfiguration conf;

    private Multimap<GridCoordinates, Stop> values = ArrayListMultimap.create();

    @NotNull
    public List<Stop> get(@NotNull GridCoordinates key, @NotNull Location location) {
        final Collection<Stop> stops = values.get(key);
        if (stops == null || stops.size() == 0) {
            return Collections.emptyList();
        }

        final List<Stop> result = new ArrayList<>(stops.size());
        for (Stop stop : stops) {
            if (Location.distance(stop.getLocation(), location) < conf.getRadiusMeters()) {
                result.add(stop);
            }
        }

        return result;
    }

}
