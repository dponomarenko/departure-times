package com.uber.departure.times.common.pojo.serializer;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.map.hazelcast.Serializer;
import com.uber.departure.times.common.pojo.Stop;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class Stop2StringSerializer implements Serializer<Stop, String> {
    public static final Serializer<Stop, String> SERIALIZER = new Stop2StringSerializer();

    @NotNull
    @Override
    public String to(@NotNull Stop value) {
        return value.toJson().encode();
    }

    @NotNull
    @Override
    public Stop from(@NotNull String value) {
        return new Stop(new JsonObject(value));
    }
}
