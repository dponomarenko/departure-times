package com.uber.departure.times.common.pojo.serializer;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.map.hazelcast.Serializer;
import com.uber.departure.times.common.pojo.ProvidedPredictions;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class ProvidedPredictions2StringSerializer implements Serializer<ProvidedPredictions, String> {
    public static final Serializer<ProvidedPredictions, String> SERIALIZER = new ProvidedPredictions2StringSerializer();

    @NotNull
    @Override
    public String to(@NotNull ProvidedPredictions value) {
        return value.toJson().encode();
    }

    @NotNull
    @Override
    public ProvidedPredictions from(@NotNull String value) {
        return new ProvidedPredictions(new JsonObject(value));
    }
}
