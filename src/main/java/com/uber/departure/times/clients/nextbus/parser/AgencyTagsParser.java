package com.uber.departure.times.clients.nextbus.parser;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
@Component
public final class AgencyTagsParser extends AJsonParser<String> {
    private AgencyTagsParser() {
        super(json -> json.getJsonArray("agency"), HashSet::new);
    }

    @NotNull
    @Override
    protected String parse(@NotNull Object o) {
        final JsonObject agency = (JsonObject) o;
        return agency.getString("tag");
    }
}
