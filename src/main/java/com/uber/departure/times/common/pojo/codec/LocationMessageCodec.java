package com.uber.departure.times.common.pojo.codec;

import com.uber.departure.times.common.pojo.Location;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author Danila Ponomarenko
 */
public final class LocationMessageCodec implements MessageCodec<Location, Location> {
    @Override
    public void encodeToWire(Buffer buffer, Location s) {
        s.toJson().writeToBuffer(buffer);
    }

    @Override
    public Location decodeFromWire(int pos, Buffer buffer) {
        return new Location(buffer.toJsonObject());
    }

    @Override
    public Location transform(Location s) {
        return s;
    }

    @Override
    public String name() {
        return "location";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
