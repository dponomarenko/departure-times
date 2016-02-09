package com.uber.departure.times.clients.nextbus.client.parser;

import java.util.ArrayList;
import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.pojo.Location;
import com.uber.departure.times.pojo.Route;
import com.uber.departure.times.pojo.Stop;
import com.uber.departure.times.pojo.Stops;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class StopsParser {
    public static final StopsParser PARSER = new StopsParser();

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
                route.getAgencyTag(),
                route.getRouteTag(),
                jStop.getString("tag"),
                new Location(
                        jStop.getDouble("lat"),
                        jStop.getDouble("lon")
                )
        );
    }
}
