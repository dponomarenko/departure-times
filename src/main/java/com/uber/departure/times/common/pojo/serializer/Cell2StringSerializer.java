package com.uber.departure.times.common.pojo.serializer;

import org.jetbrains.annotations.NotNull;

import com.uber.departure.times.common.map.hazelcast.Serializer;
import com.uber.departure.times.common.pojo.Cell;

import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class Cell2StringSerializer implements Serializer<Cell, String> {
    public static final Serializer<Cell, String> SERIALIZER = new Cell2StringSerializer();

    @NotNull
    @Override
    public String to(@NotNull Cell value) {
        return value.toJson().encode();
    }

    @NotNull
    @Override
    public Cell from(@NotNull String value) {
        return new Cell(new JsonObject(value));
    }
}
