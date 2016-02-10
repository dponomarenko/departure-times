package com.uber.departure.times.common.pojo.serializer;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.map.hazelcast.Serializer;
import com.uber.departure.times.common.pojo.StopId;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class StopId2StringSerializer implements Serializer<StopId, String> {
    public static final Serializer<StopId, String> SERIALIZER = new StopId2StringSerializer();

    @NotNull
    @Override
    public String to(@NotNull StopId value) {
        return value.toJson().encode();
    }

    @NotNull
    @Override
    public StopId from(@NotNull String value) {
        return new StopId(new JsonObject(value));
    }
}
