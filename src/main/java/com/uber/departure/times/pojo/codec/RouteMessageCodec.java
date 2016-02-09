package com.uber.departure.times.pojo.codec;

import com.uber.departure.times.pojo.Route;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author Danila Ponomarenko
 */
public final class RouteMessageCodec implements MessageCodec<Route, Route> {
    @Override
    public void encodeToWire(Buffer buffer, Route r) {
        r.toJson().writeToBuffer(buffer);
    }

    @Override
    public Route decodeFromWire(int pos, Buffer buffer) {
        return new Route(buffer.toJsonObject());
    }

    @Override
    public Route transform(Route r) {
        return r;
    }

    @Override
    public String name() {
        return "route";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}