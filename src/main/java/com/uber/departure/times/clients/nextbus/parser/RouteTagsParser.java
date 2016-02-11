package com.uber.departure.times.clients.nextbus.parser;

import java.util.Arrays;
import java.util.HashSet;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class RouteTagsParser extends AJsonParser<String> {
    private RouteTagsParser() {
        super(json -> {
            //nextbus api bug: when there is only one route it returns object instead of array
            final Object route = json.getValue("route");
            if (route instanceof JsonArray) {
                return (JsonArray) route;
            } else {
                return new JsonArray(Arrays.asList(route));
            }
        }, HashSet::new);
    }

    @NotNull
    @Override
    protected String parse(@NotNull Object o) {
        final JsonObject route = (JsonObject) o;
        return route.getString("tag");
    }
}
