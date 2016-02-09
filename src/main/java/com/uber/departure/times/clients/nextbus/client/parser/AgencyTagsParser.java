package com.uber.departure.times.clients.nextbus.client.parser;

import java.util.HashSet;

import org.jetbrains.annotations.NotNull;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class AgencyTagsParser extends AJsonParser<String> {
    public static final AgencyTagsParser PARSER = new AgencyTagsParser();

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
