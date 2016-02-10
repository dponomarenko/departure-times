package com.uber.departure.times.clients.nextbus.parser;

import java.util.ArrayList;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import com.uber.departure.times.common.pojo.Location;
import com.uber.departure.times.common.pojo.Route;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.StopId;
import com.uber.departure.times.common.pojo.Stops;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class StopsParser {
    public Stops parse(@NotNull Route route, @NotNull Buffer buffer) {
        final JsonObject json = buffer.toJsonObject();
        final Collection<Stop> result = new ArrayList<>();
        for (Object o : json.getJsonObject("route").getJsonArray("stop")) {
            result.add(parse(route, o));
        }
        return new Stops(result);
    }

    @NotNull
    private Stop parse(@NotNull Route route, @NotNull Object o) {
        final JsonObject jStop = (JsonObject) o;
        return new Stop(
                new StopId(route.getAgencyTag(),
                        route.getRouteTag(),
                        jStop.getString("tag")),
                new Location(
                        Double.valueOf(jStop.getString("lat")),
                        Double.valueOf(jStop.getString("lon"))
                )
        );
    }
}
