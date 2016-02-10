package com.uber.departure.times.clients.nextbus.parser;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class RouteTagsParser extends AJsonParser<String> {
    private RouteTagsParser() {
        super(json -> json.getJsonArray("route"), HashSet::new);
    }

    @NotNull
    @Override
    protected String parse(@NotNull Object o) {
        final JsonObject route = (JsonObject) o;
        return route.getString("tag");
    }
}
