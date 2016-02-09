package com.uber.departure.times.clients.nextbus.client.parser;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class RouteTagsParser extends AJsonParser<String> {
    public static final RouteTagsParser PARSER = new RouteTagsParser();

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
