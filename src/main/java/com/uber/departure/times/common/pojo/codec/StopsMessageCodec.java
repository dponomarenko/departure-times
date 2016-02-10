package com.uber.departure.times.common.pojo.codec;

import com.uber.departure.times.common.JsonConvertible;
import com.uber.departure.times.common.pojo.Stop;
import com.uber.departure.times.common.pojo.Stops;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

/**
 * @author Danila Ponomarenko
 */
public final class StopsMessageCodec implements MessageCodec<Stops, Stops> {
    @Override
    public void encodeToWire(Buffer buffer, Stops s) {
        s.toJsonArray().writeToBuffer(buffer);
    }

    @Override
    public Stops decodeFromWire(int pos, Buffer buffer) {
        return new Stops(JsonConvertible.fromJsonArray(buffer.toJsonArray(), o -> new Stop((JsonObject) o)));
    }

    @Override
    public Stops transform(Stops s) {
        return s;
    }

    @Override
    public String name() {
        return "stops";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}